package com.hubject.chargingstation.service.predicate;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.Set;

// only the fields that I want
// we use record so it is immutable
@Builder(toBuilder = true)
public record ChargingStationFilters(
        // i added set so user can filter multiple
        Set<String> chargerName,                  // LIKE predicate
        Set<BigDecimal> power,                    // equal predicate
        CoordinateFilter coordinate     // latitude, longitude -> equal predicate
) {
}


