package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.filter.Filter;
import com.hubject.chargingstation.filter.FilterResolver;
import com.hubject.chargingstation.service.predicate.ChargingStationFilters;
import com.hubject.chargingstation.service.predicate.CoordinateFilter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Component
@Slf4j
public class SpecificationResolver {

    private final List<Filter<ChargingStation, ChargingStationFilters>> predicates;

   public Specification<ChargingStation> resolveSpecification(ChargingStationRequestDto chargingStationRequestDto){

       var coordinateFilter = chargingStationRequestDto.getCoordinate() != null
               ? CoordinateFilter.builder()
               .latitude(chargingStationRequestDto.getCoordinate().getLatitude())
               .longitude(chargingStationRequestDto.getCoordinate().getLongitude())
               .build()
               : null;

       var filter = ChargingStationFilters.builder()
               .coordinate(coordinateFilter)
               .chargerName(chargingStationRequestDto.getChargerName() != null
                       ? Set.of(chargingStationRequestDto.getChargerName())
                       : null)
               .power(chargingStationRequestDto.getPower() != null
                       ? Set.of(chargingStationRequestDto.getPower())
                       : null)
               .build();

         return FilterResolver.execute(predicates, filter);
   }

    @PostConstruct
    public void logPredicates() {
        log.info("Registered predicates: {}", predicates);
    }

}
