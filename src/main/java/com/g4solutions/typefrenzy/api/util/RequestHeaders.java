package com.g4solutions.booksbuilder.api.util;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestHeaders {

  public static final String PAGE_PARAM = "page";

  public static final String BOOK_SLUG_PARAM = "bookSlug";

  public static final String TEMPLATE_TYPE_PARAM = "templateType";

  public static final String DELETED_PARAM = "deleted";

  public static final String SHOW_ALL_PARAM = "showAll";

  public static final String PAGE_SIZE_PARAM = "pageSize";

  public static Optional<String> getQueryParam(String queryParamName,
      Map<String, Object> queryParameters) {

    if (Objects.isNull(queryParameters)) {
      return Optional.empty();
    }

    return Optional
        .ofNullable(queryParameters.get(queryParamName))
        .map(String::valueOf);
  }
}
