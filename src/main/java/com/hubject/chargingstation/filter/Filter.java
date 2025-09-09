package com.hubject.chargingstation.filter;


import java.util.Optional;
import java.util.Set;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


import static java.lang.String.format;

//a generic contract for given a filter object
public interface Filter<Y, T> {

    String LIKE_PATTERN = "%%%s%%";

    Optional<Predicate> getPredicate(Root<Y> root, CriteriaQuery<?> query, CriteriaBuilder cb, T filter);

    static Optional<Predicate> getMultipleLikePredicate(Set<String> searchedValues, Path<?> path, CriteriaBuilder cb) {

        Predicate disjunction = cb.disjunction();
        if (searchedValues != null && !searchedValues.isEmpty()) {
            for (String value : searchedValues) {
                disjunction.getExpressions().add(cb.like(cb.lower(path.as(String.class)), format(LIKE_PATTERN, value.toLowerCase())));
            }
        }
        return !disjunction.getExpressions().isEmpty() ? Optional.of(disjunction) : Optional.empty();
    }
}