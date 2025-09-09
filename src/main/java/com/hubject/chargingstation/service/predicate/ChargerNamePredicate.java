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
public class ChargerNamePredicate implements Filter<ChargingStation, ChargingStationFilters> {

    // my conditions by Predicate (where condition)
    @Override
    public Optional<Predicate> getPredicate(Root<ChargingStation> root, CriteriaQuery<?> query,
                                            CriteriaBuilder cb, ChargingStationFilters filter) {
        return Filter.getMultipleLikePredicate(filter.chargerName(), root.get(ChargingStationAttributes.CHARGER_NAME), cb);
    }

}

