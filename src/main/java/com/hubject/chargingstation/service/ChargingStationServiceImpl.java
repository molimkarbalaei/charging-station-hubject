package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.entity.GoogleCoordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j

public class ChargingStationServiceImpl implements ChargingStationService {

    // private final ChargingStationRepository repository;

    private final List<ChargingStation> fackDB = new ArrayList<>(
            List.of(
                    new ChargingStation("1", "Station A", BigDecimal.valueOf(50), new GoogleCoordinate(48.8566, 2.3522)),
                    new ChargingStation("2", "Station B", BigDecimal.valueOf(75), new GoogleCoordinate(51.5074, -0.1278)),
                    new ChargingStation("3", "Station C", BigDecimal.valueOf(100), new GoogleCoordinate(40.7128, -74.0060))
            )

    );

    // I need to convert DTO to entity and vice versa

    // entity to DTO
    private ChargingStationResponseDto responseDto(ChargingStation station) {
        ChargingStationResponseDto stationDto = new ChargingStationResponseDto();
        stationDto.setId(station.getId());
        stationDto.setChargerName(station.getChargerName());
        stationDto.setPower(station.getPower());
        stationDto.setCoordinate(station.getCoordinate());
        return stationDto;
    }

    // DTO to entity
    private ChargingStation toEntity(ChargingStationRequestDto stationDto) {
        return new ChargingStation(
                null,
                stationDto.getChargerName(),
                stationDto.getPower(),
                stationDto.getCoordinate()
        );
    }

    @Override
    public List<ChargingStationResponseDto> getChargingStations(ChargingStationRequestDto chargingStationRequestDto) {
        log.info("Getting charging stations with request: {}", chargingStationRequestDto);

        // no filter
        if (chargingStationRequestDto.getId() == null &&
                chargingStationRequestDto.getPower() == null &&
                (chargingStationRequestDto.getCoordinate() == null ||
                        (chargingStationRequestDto.getCoordinate().getLatitude() == null &&
                                chargingStationRequestDto.getCoordinate().getLongitude() == null))) {
            return getAllChargingStations();
        }
        // filter by chargingsstationsdto
        return filterChargingStations(chargingStationRequestDto);
    }


    public List<ChargingStationResponseDto> getAllChargingStations() {
        return fackDB.stream()
                .map(this::responseDto)
                .collect(Collectors.toList());

    }

    public List<ChargingStationResponseDto> filterChargingStations(ChargingStationRequestDto chargingStationRequestDto) {
        return fackDB.stream()
                .filter(station -> (chargingStationRequestDto.getId() == null || station.getId().equals(chargingStationRequestDto.getId())) &&
                        (chargingStationRequestDto.getPower() == null || station.getPower().compareTo(chargingStationRequestDto.getPower()) == 0) &&
                        (chargingStationRequestDto.getCoordinate() == null || station.getCoordinate().equals(chargingStationRequestDto.getCoordinate())))
                .map(this::responseDto)
                .collect(Collectors.toList());

    }

    @Override
    public ChargingStationResponseDto getById(String id) {
        return fackDB.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .map(this::responseDto)
                .orElse(null);
    }

    @Override
    public ChargingStationResponseDto saveChargingStation(ChargingStationRequestDto chargingStationDto) {
        log.info("Saving charging station {}", chargingStationDto.getChargerName());
        // we get the input from fe and convert it to sth we can save which is entity
        ChargingStation entity = toEntity(chargingStationDto);
        entity.setId(UUID.randomUUID().toString());
        // save to fackDB
        fackDB.add(entity);
        return responseDto(entity);
    }

    @Override
    public ChargingStationResponseDto updateChargingStation(String id, ChargingStationRequestDto chargingStationDto) {
        log.info("Updating charging station {}", chargingStationDto.getChargerName());
        // we already have the id and we have to update the entity so I have to find the id and update the data
        for (ChargingStation station : fackDB) {
            if (station.getId().equals(id)) {
                // update the data
                station.setChargerName(chargingStationDto.getChargerName());
                station.setPower(chargingStationDto.getPower());
                station.setCoordinate(chargingStationDto.getCoordinate());
                return responseDto(station);
            }
        }
        // if not found return null or throw the exception
        return null;
    }

    @Override
    public void deleteChargingStation(String id) {
        log.info("Deleting charging station {}", id);
        log.error("Error deleting charging station {}", id);
        fackDB.removeIf(station -> station.getId().equals(id));
    }
}
