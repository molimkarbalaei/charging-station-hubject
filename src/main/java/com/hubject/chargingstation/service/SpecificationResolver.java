package com.hubject.chargingstation.service;

import com.hubject.chargingstation.dto.ChargingStationRequestDto;
import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.filter.Filter;
import com.hubject.chargingstation.filter.FilterResolver;
import com.hubject.chargingstation.service.predicate.ChargingStationFilters;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
@Slf4j
public class SpecificationResolver {

    private final List<Filter<ChargingStation, ChargingStationFilters>> predicates;

    public Specification<ChargingStation> resolveSpecification(ChargingStationRequestDto chargingStationRequestDto){

        var filter = ChargingStationFilters.builder()
                .zipcode(Optional.ofNullable(chargingStationRequestDto.getZipcode()).filter(s -> !s.isBlank()).orElse(null))
                .chargerName(Optional.ofNullable(chargingStationRequestDto.getChargerName())
                        .filter(s -> !s.isBlank())
                        .map(name -> Set.of(name))
                        .orElse(null))
                .power(chargingStationRequestDto.getPower())
                .build();

        return FilterResolver.execute(predicates, filter);
    }

    @PostConstruct
    public void logPredicates() {
        log.info("Registered predicates: {}", predicates);
    }

}