package com.hubject.chargingstation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCoordinate {
    private Double latitude;
    private Double longitude;
}
