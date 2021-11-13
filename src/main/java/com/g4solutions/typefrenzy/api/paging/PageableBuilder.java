package com.g4solutions.typefrenzy.api.paging;

import com.g4solutions.booksbuilder.api.util.RequestHeaders;
import com.g4solutions.typefrenzy.configuration.TypeFrenzyConfigProperties;
import com.g4solutions.typefrenzy.domain.AbstractEntity;
import com.g4solutions.typefrenzy.util.SpringContextHolder;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableBuilder {

  public static Pageable buildFrom(Map<String, Object> queryParameters, Class<?> entityType,
      int maxCount) {
    int page = RequestHeaders.getQueryParam(RequestHeaders.PAGE_PARAM, queryParameters)
        .map(pageParam -> PageableBuilder
            .parseToInteger(pageParam)
            .orElse(0)
        )
        .orElse(0);

    int pageSize = RequestHeaders.getQueryParam(RequestHeaders.PAGE_SIZE_PARAM, queryParameters)
        .map(pageSizeParam -> PageableBuilder
            .parseToInteger(pageSizeParam)
            .orElse(PageableBuilder.defaultPageSize())
        )
        .orElse(PageableBuilder.defaultPageSize());

    boolean showAll = RequestHeaders.getQueryParam(RequestHeaders.SHOW_ALL_PARAM, queryParameters)
        .map(Boolean::valueOf)
        .orElse(false);

    if (showAll) {
      page = 0;
      pageSize = maxCount > 0 ? maxCount : 1;
    }

    if (AbstractEntity.class.isAssignableFrom(entityType)) {
      return PageRequest.of(page, pageSize,
          Sort.by(Order.desc("createdAt"), Order.desc("priority")));
    }

    return PageRequest.of(page, pageSize);
  }

  private static Integer defaultPageSize() {
    return SpringContextHolder
        .getBean(TypeFrenzyConfigProperties.class)
        .defaultPageSize();
  }

  private static Optional<Integer> parseToInteger(Object value) {
    if (value instanceof String) {
      try {
        return Optional.of(Integer.parseInt((String) value));
      } catch (NumberFormatException ex) {
        return Optional.empty();
      }
    } else if (value.getClass().isAssignableFrom(Integer.class) ||
        value.getClass().isAssignableFrom(int.class)) {
      return Optional.of((Integer) value);
    }
    return Optional.empty();
  }

}
