package com.hubject.chargingstation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStation {

    private String id;
    private String chargerName;
    private BigDecimal power;
    private GoogleCoordinate coordinate;
}
