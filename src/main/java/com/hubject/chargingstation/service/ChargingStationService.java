package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;

import java.util.List;
import java.util.Optional;

public interface ChargingStationService {

    List<ChargingStationResponseDto> getChargingStations(ChargingStationRequestDto chargingStationRequestDto);

    Optional<ChargingStationResponseDto> getById(String id);

    ChargingStationResponseDto saveChargingStation(ChargingStationRequestDto chargingStationDto);

    ChargingStationResponseDto updateChargingStation(String id, ChargingStationRequestDto chargingStationDto);

    void deleteChargingStation(String id);
}
