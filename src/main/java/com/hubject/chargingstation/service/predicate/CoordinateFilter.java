package com.hubject.chargingstation.service.predicate;

import lombok.Builder;

@Builder
public record CoordinateFilter(Double latitude, Double longitude) {
}
