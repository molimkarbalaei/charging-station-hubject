package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.repository.ChargingStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChargingStationServiceImpl implements ChargingStationService {

    private final ChargingStationRepository repository;
    private final SpecificationResolver specificationResolver;
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    @Transactional(readOnly = true)
    @Override
    public List<ChargingStationResponseDto> getChargingStations(ChargingStationRequestDto chargingStationRequestDto) {
        log.info("Getting charging stations with request: {}", chargingStationRequestDto);

        // no filter
        if (chargingStationRequestDto.getId() == null &&
                (chargingStationRequestDto.getChargerName() == null || chargingStationRequestDto.getChargerName().isBlank()) &&
                (chargingStationRequestDto.getPower() == null) &&
                (chargingStationRequestDto.getZipcode() == null || chargingStationRequestDto.getZipcode().isBlank()) &&
                (chargingStationRequestDto.getCoordinate() == null ||
                        (chargingStationRequestDto.getCoordinate().getLatitude() == null &&
                                chargingStationRequestDto.getCoordinate().getLongitude() == null))) {
            return getAllChargingStations();
        }
        // filter by ChargingStationRequestDto
        return filterChargingStations(chargingStationRequestDto);
    }

    public List<ChargingStationResponseDto> getAllChargingStations() {
        return repository.findAll().stream()
                .map(this::responseDto)
                .collect(Collectors.toList());
    }

    // filter url for lang long and distance : // charging-stations?latitude=48.7&longitude=9.11&distance=10
    public List<ChargingStationResponseDto> filterChargingStations(ChargingStationRequestDto chargingStationRequestDto) {
        log.info("Filtering charging stations with request: {}", chargingStationRequestDto);
        var specification = specificationResolver.resolveSpecification(chargingStationRequestDto);
        var cs = repository.findAll(specification);

        if (chargingStationRequestDto.getCoordinate() != null && chargingStationRequestDto.getCoordinate().getLatitude() != null && chargingStationRequestDto.getCoordinate().getLongitude() != null) {
            double maxDistance = chargingStationRequestDto.getDistanceKm() != null
                    ? chargingStationRequestDto.getDistanceKm()
                    : Double.MAX_VALUE;
            double userLat = chargingStationRequestDto.getCoordinate().getLatitude();
            double userLon = chargingStationRequestDto.getCoordinate().getLongitude();

            cs = cs.stream()
                    .filter(station -> {
                        if (station.getCoordinate() == null ||
                                station.getCoordinate().getLatitude() == null ||
                                station.getCoordinate().getLongitude() == null) {
                            return false;
                        }
                        double stationDis = calculateDistance(
                                userLat,
                                userLon,
                                station.getCoordinate().getLatitude(),
                                station.getCoordinate().getLongitude()
                        );
                        log.info("Station {} is {} km away", station.getChargerName(), stationDis);
                        return stationDis <= maxDistance;
                    }).toList();
        }
        log.info("filtered charging stations: {}", cs);
        return cs.stream().map(this::responseDto).toList();
    }

    @Override
    public Optional<ChargingStationResponseDto> getById(String id) {
        return repository.findById(id).map(this::responseDto);
        // use optional to handel null better
    }

    @Transactional
    @Override
    public ChargingStationResponseDto saveChargingStation(ChargingStationRequestDto chargingStationDto) {
        log.info("Saving charging station {}", chargingStationDto.getChargerName());
        // we get the input from fe and convert it to sth we can save which is entity
        ChargingStation entity = toEntity(chargingStationDto);
        entity.setId(UUID.randomUUID().toString());
        return responseDto(repository.save(entity));
    }

    @Transactional
    @Override
    public ChargingStationResponseDto updateChargingStation(String id, ChargingStationRequestDto chargingStationDto) {
        log.info("Updating charging station {}", chargingStationDto.getChargerName());
        // we already have the id and we have to update the entity so I have to find the id and update the data

        return repository.findById(id)
                .map(station -> {
                    // update the data
                    station.setChargerName(chargingStationDto.getChargerName());
                    station.setPower(chargingStationDto.getPower());
                    station.setCoordinate(chargingStationDto.getCoordinate());
                    station.setZipcode(chargingStationDto.getZipcode());
                    return responseDto(station);
                })
                .orElseThrow(() -> new RuntimeException("Charging station not found with id: " + id));
    }

    @Transactional
    @Override
    public void deleteChargingStation(String id) {
        log.info("Deleting charging station {}", id);
        log.error("Error deleting charging station {}", id);
        repository.deleteById(id);
    }

    // I need to convert DTO to entity and vice versa

    // entity to DTO
    private ChargingStationResponseDto responseDto(ChargingStation station) {
        ChargingStationResponseDto stationDto = new ChargingStationResponseDto();
        stationDto.setId(station.getId());
        stationDto.setChargerName(station.getChargerName());
        stationDto.setPower(station.getPower());
        stationDto.setCoordinate(station.getCoordinate());
        stationDto.setZipcode(station.getZipcode());
        return stationDto;
    }

    // DTO to entity
    private ChargingStation toEntity(ChargingStationRequestDto stationDto) {
        return new ChargingStation(
                null,
                stationDto.getChargerName(),
                stationDto.getPower(),
                stationDto.getCoordinate(),
                stationDto.getZipcode()
        );
    }

    // harvesine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }

}
