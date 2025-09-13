package com.hubject.chargingstation.service.predicate;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.Set;

// only the fields that I want
// we use record so it is immutable
@Builder(toBuilder = true)
public record ChargingStationFilters(
        Set<String> chargerName,
        BigDecimal power,
        Double distanceKm,
        String zipcode
) { }



