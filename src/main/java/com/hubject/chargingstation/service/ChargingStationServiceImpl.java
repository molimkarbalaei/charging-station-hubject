package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.entity.GoogleCoordinate;
import com.hubject.chargingstation.repository.ChargingStationRepository;
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

     private final ChargingStationRepository repository;

     public ChargingStationServiceImpl(ChargingStationRepository repository) {
         this.repository = repository;
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
        return repository.findAll().stream()
                .map(this::responseDto)
                .collect(Collectors.toList());

    }

    public List<ChargingStationResponseDto> filterChargingStations(ChargingStationRequestDto chargingStationRequestDto) {
         // i know it is not good way to filter by findAll TODO: chnage later
        return repository.findAll().stream()
                .filter(station ->
                        (chargingStationRequestDto.getId() == null || station.getId().equals(chargingStationRequestDto.getId())) &&
                        (chargingStationRequestDto.getPower() == null || station.getPower().compareTo(chargingStationRequestDto.getPower()) == 0) &&
                        (chargingStationRequestDto.getCoordinate() == null || station.getCoordinate().equals(chargingStationRequestDto.getCoordinate())) ||
                                (
                                        chargingStationRequestDto.getCoordinate() != null &&
                                        (chargingStationRequestDto.getCoordinate().getLongitude() == null || station.getCoordinate().getLongitude().compareTo(chargingStationRequestDto.getCoordinate().getLongitude()) == 0) &&
                                                (chargingStationRequestDto.getCoordinate().getLatitude() == null || station.getCoordinate().getLatitude().compareTo(chargingStationRequestDto.getCoordinate().getLatitude()) == 0)

                                )
               )
                .map(this::responseDto)
                .collect(Collectors.toList());

    }

    @Override
    public ChargingStationResponseDto getById(String id) {
        return repository.findById(id)
                .map(this::responseDto)
                .orElse(null);
    }

    @Override
    public ChargingStationResponseDto saveChargingStation(ChargingStationRequestDto chargingStationDto) {
        log.info("Saving charging station {}", chargingStationDto.getChargerName());
        // we get the input from fe and convert it to sth we can save which is entity
        ChargingStation entity = toEntity(chargingStationDto);
        entity.setId(UUID.randomUUID().toString());
        return responseDto(repository.save(entity));
    }

    @Override
    public ChargingStationResponseDto updateChargingStation(String id, ChargingStationRequestDto chargingStationDto) {
        log.info("Updating charging station {}", chargingStationDto.getChargerName());
        // we already have the id and we have to update the entity so I have to find the id and update the data

      return repository.findById(id)
                .map(station ->{
                    // update the data
                    station.setChargerName(chargingStationDto.getChargerName());
                    station.setPower(chargingStationDto.getPower());
                    station.setCoordinate(chargingStationDto.getCoordinate());
                    return responseDto(station);
                })
                .orElseThrow(() -> new RuntimeException("Charging station not found with id: " + id));
    }

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
}
