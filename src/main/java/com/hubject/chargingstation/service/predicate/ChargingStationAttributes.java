package com.hubject.chargingstation.service.predicate;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
//helper for strings
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ChargingStationAttributes {
    static final String CHARGER_NAME = "chargerName";
    static final String POWER = "power";
    static final String COORDINATE = "coordinate";
    static final String LATITUDE = "latitude";
    static final String LONGITUDE = "longitude";
}