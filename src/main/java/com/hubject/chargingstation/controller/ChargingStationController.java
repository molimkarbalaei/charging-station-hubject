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


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping (value = "api/v1/charging-stations",  produces = MediaType.APPLICATION_JSON_VALUE)
public class ChargingStationController {

private final ChargingStationService service;

@GetMapping
    public ResponseEntity<List<ChargingStationResponseDto>> getAllChargingStations(
        @RequestParam(required = false) String id,
        @RequestParam(required = false) BigDecimal power,
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude
) {
    GoogleCoordinate coordinate = null;
    if (latitude != null && longitude != null) {
        coordinate = new GoogleCoordinate(latitude, longitude);
    }
    // This method returns all charging stations
    if (id == null && power == null && coordinate == null) {
        return ResponseEntity.ok(service.getAllChargingStations());
    }else {
        return ResponseEntity.ok(service.filterChargingStations(id, power, coordinate));
    }
}

@GetMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDto> getChargingStationById(@PathVariable("id") String id) {
    ChargingStationResponseDto station = service.getById(id);
// TODO: add exception handler later and remove this null checking
    if (station == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(station);
    }

@PostMapping
    public ResponseEntity<ChargingStationResponseDto> create(@RequestBody @Valid ChargingStationRequestDto chargingStationDto) {
    return new ResponseEntity<>(service.saveChargingStation(chargingStationDto), HttpStatus.CREATED);
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveChargingStation(chargingStationDto));
}

@PutMapping("/{id}")
    public ResponseEntity<ChargingStationResponseDto> update(@PathVariable("id") String id, @RequestBody @Valid ChargingStationRequestDto chargingStationDto) {
    ChargingStationResponseDto updateStation = service.updateChargingStation(id, chargingStationDto);
    // TODO: add exception handler later and remove this null checking
    if (updateStation == null) {
        return ResponseEntity.notFound().build();
    }
    return  ResponseEntity.ok(updateStation);
}

@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
    service.deleteChargingStation(id);
    return ResponseEntity.noContent().build();
    // or return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    }




