package com.hubject.chargingstation.filter;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FilterResolver {

    public static <Y, T> Specification<Y> execute(List<Filter<Y, T>> filterList, T filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = filterList.stream()
                    .map(f -> f.getPredicate(root, query, cb, filter))
                    .flatMap(Optional::stream)
                    .toList();
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}