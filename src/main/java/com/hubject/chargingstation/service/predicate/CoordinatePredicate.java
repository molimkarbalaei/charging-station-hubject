package com.hubject.chargingstation.service.predicate;

import com.hubject.chargingstation.entity.ChargingStation;
import com.hubject.chargingstation.filter.Filter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CoordinatePredicate implements Filter<ChargingStation, ChargingStationFilters> {

    @Override
    public Optional<Predicate> getPredicate(Root<ChargingStation> root, CriteriaQuery<?> query, CriteriaBuilder cb, ChargingStationFilters filter) {

        return Optional.ofNullable(filter.coordinate())
                .map(coordinates -> cb.and(
                        cb.equal(root.get(ChargingStationAttributes.COORDINATE)
                                .get(ChargingStationAttributes.LATITUDE), coordinates.latitude()),
                        cb.equal(root.get(ChargingStationAttributes.COORDINATE)
                                .get(ChargingStationAttributes.LONGITUDE), coordinates.longitude())
                ));
    }
}
