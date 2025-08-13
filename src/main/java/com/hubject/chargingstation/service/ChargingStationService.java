package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.GoogleCoordinate;

import java.math.BigDecimal;
import java.util.List;

public interface ChargingStationService {

    List<ChargingStationResponseDto> getAllChargingStations();
    List<ChargingStationResponseDto> filterChargingStations(String id, BigDecimal power, GoogleCoordinate coordinate);
    ChargingStationResponseDto getById(String id);


    ChargingStationResponseDto saveChargingStation(ChargingStationRequestDto chargingStationDto);
    ChargingStationResponseDto updateChargingStation(String id, ChargingStationRequestDto chargingStationDto);
    void deleteChargingStation(String id);

}
