package com.hubject.chargingstation.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "charging_station")
public class ChargingStation {

    @Id
    private String id;
    @Column(name = "name")
    private String chargerName;
    private BigDecimal power;
    @Embedded
    private GoogleCoordinate coordinate;
    private String zipcode;
}
