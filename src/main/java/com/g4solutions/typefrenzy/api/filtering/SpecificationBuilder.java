package com.g4solutions.typefrenzy.api.filtering;

import com.g4solutions.typefrenzy.api.filtering.param.FilteringParam;
import com.g4solutions.booksbuilder.api.util.RequestHeaders;
import com.g4solutions.typefrenzy.util.SpringContextHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SpecificationBuilder {

  @SuppressWarnings("unchecked")
  public static <T> Specification<T> buildFrom(
      HttpHeaders headers,
      Map<String, Object> queryParameters,
      Class<T> specificationType
  ) {

    List<Specification<T>> specifications = new ArrayList<>();

    for (String header : headers.keySet()) {

      List<Specification<T>> specification = buildSpecification(
          headers,
          specificationType,
          header
      );

      specifications.addAll(specification);
    }

    for (String header : queryParameters.keySet()) {
      List<Specification<T>> specification = buildSpecificationFromQueryParams(
          queryParameters,
          specificationType,
          header
      );

      specifications.addAll(specification);
    }

    SpecificationBuilder
        .resolveNotDeletedFilter(headers, specificationType)
        .ifPresent(specifications::add);

    Specification<T> specification = Specification.where(null);
    boolean isWhereClauseSet = false;

    for (Specification<T> currSpecification : specifications) {
      if (!isWhereClauseSet) {
        specification = Specification.where(currSpecification);
        isWhereClauseSet = true;
      } else {
        specification = specification.and(currSpecification);
      }
    }

    return specification;
  }

  @SuppressWarnings("unchecked")
  private static <T> Optional<Specification<T>> resolveNotDeletedFilter(
      HttpHeaders httpHeaders,
      Class<T> specificationType
  ) {
    boolean deletedFilterApplied = httpHeaders.containsKey(
        RequestHeaders.DELETED_PARAM
    );

    if (!deletedFilterApplied) {
      return SpringContextHolder
          .getBeansByGenericParameters(
              FilteringParam.class,
              specificationType
          )
          .stream()
          .filter(filteringParam -> RequestHeaders.DELETED_PARAM
              .equals(filteringParam.name())
          )
          .findFirst()
          .map(deletedFilteringParam ->
              (Specification<T>) deletedFilteringParam.apply(Boolean.FALSE.toString())
          );
    }

    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Specification<T>> buildSpecification(
      HttpHeaders headers,
      Class<T> specificationType,
      String header) {

    return Collections.singletonList(
        SpringContextHolder
            .getBeansByGenericParameters(FilteringParam.class, specificationType)
            .stream()
            .filter(filteringParam -> header.equalsIgnoreCase(filteringParam.name()))
            .findFirst()
            .map(param -> param.apply(headers.get(header)))
            .orElse(null)
    );
  }

  @SuppressWarnings("unchecked")
  private static <T> List<Specification<T>> buildSpecificationFromQueryParams(
      Map<String, Object> queryParameters,
      Class<T> specificationType,
      String header) {

    return Collections.singletonList(
        SpringContextHolder
            .getBeansByGenericParameters(FilteringParam.class, specificationType)
            .stream()
            .filter(filteringParam -> header.equalsIgnoreCase(filteringParam.name()))
            .findFirst()
            .map(param -> param.apply(queryParameters.get(header)))
            .orElse(null)
    );
  }
}