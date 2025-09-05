package com.hubject.chargingstation.controller;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.dto.ChargingStationResponseDto;
import com.hubject.chargingstation.entity.GoogleCoordinate;
import com.hubject.chargingstation.service.ChargingStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/charging-stations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChargingStationController {

    private final ChargingStationService service;

    @GetMapping
    public ResponseEntity<List<ChargingStationResponseDto>> getAllChargingStations(
            @ModelAttribute ChargingStationRequestDto chargingStationRequestDto
    ) {
        return ResponseEntity.ok(service.getChargingStations(chargingStationRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDto> getChargingStationById(@PathVariable("id") String id) {
        ChargingStationResponseDto station = service.getById(id);
        return ResponseEntity.ok(station);
    }

    //TODO: spring only alows me to add one to the json so if I want to add more
    @PostMapping
    public ResponseEntity<ChargingStationResponseDto> create(@RequestBody @Valid ChargingStationRequestDto chargingStationDto) {
        return new ResponseEntity<>(service.saveChargingStation(chargingStationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid ChargingStationRequestDto chargingStationDto) {
        ChargingStationResponseDto updateStation = service.updateChargingStation(id, chargingStationDto);
        return ResponseEntity.ok(updateStation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.deleteChargingStation(id);
        return ResponseEntity.noContent().build();
    }
}




