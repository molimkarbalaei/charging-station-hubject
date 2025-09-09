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
public class PowerPredicate implements Filter<ChargingStation, ChargingStationFilters> {

    // where CHARGER_NAME is like %molim% or power = 50
    @Override
    public Optional<Predicate> getPredicate(Root<ChargingStation> root, CriteriaQuery<?> query, CriteriaBuilder cb, ChargingStationFilters filter) {
        if (filter.power() == null){
            return Optional.empty();
        }
        return Optional.of(cb.equal(root.get(ChargingStationAttributes.POWER), filter.power()));
    }
}
