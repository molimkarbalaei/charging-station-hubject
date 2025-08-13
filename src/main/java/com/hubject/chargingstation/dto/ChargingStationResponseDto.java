package com.hubject.chargingstation.dto;


import com.hubject.chargingstation.entity.GoogleCoordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStationResponseDto {
    private String id;
    private String chargerName;
    private BigDecimal power;
    private GoogleCoordinate coordinate;
}
