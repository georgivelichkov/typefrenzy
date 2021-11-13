package com.g4solutions.typefrenzy.api.filtering.param;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public abstract class FilteringParam<T> {

  public Specification<T> apply(Object filterValue) {
    try {
      return this.initialize(filterValue);
    } catch (Exception ex) {
      FilteringParam.log.error(
          "Error while applying {}: {}. Skipping...",
          this.getClass().getSimpleName(),
          ex.getMessage()
      );
      return null;
    }
  }

  public abstract String name();

  protected abstract Specification<T> initialize(Object filterValue);

}