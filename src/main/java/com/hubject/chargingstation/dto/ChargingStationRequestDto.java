package com.hubject.chargingstation.dto;


import com.hubject.chargingstation.entity.GoogleCoordinate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargingStationRequestDto {
    // validation
    @NotBlank(message = "Charger name must not be blank")
    @Size(min=1, max=100, message = "Charger name must be between 1 and 100 characters")
    private String chargerName;
    private String id;
    @DecimalMin(value = "0.1", message = "Power must be at least 0.1 kW")
    private BigDecimal power;
    @Valid
    private GoogleCoordinate coordinate;
}
