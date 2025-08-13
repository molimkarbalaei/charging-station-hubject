package com.hubject.chargingstation.dto;


import com.hubject.chargingstation.entity.GoogleCoordinate;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargingStationRequestDto {
    private String id;
    private String chargerName;
    private BigDecimal power;
    private GoogleCoordinate coordinate;
}
