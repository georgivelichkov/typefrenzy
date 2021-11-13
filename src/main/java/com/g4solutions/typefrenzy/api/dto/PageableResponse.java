package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public abstract class PageableResponse<T, R> {

  @JsonProperty("page")
  private Integer page;

  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("count")
  private Long count;

  @JsonProperty("totalPages")
  private Integer totalPages;

  @JsonProperty("data")
  private List<R> data;

  protected PageableResponse(Page<T> page) {
    this.page = page.getNumber();
    this.pageSize = page.getNumberOfElements();
    this.count = page.getTotalElements();
    this.totalPages = page.getTotalPages();
    this.data = this.convertToResponse(page.getContent());
  }

  public abstract Function<T, R> responseConverter();

  private List<R> convertToResponse(List<T> pageContent) {
    return pageContent
        .stream()
        .map(this.responseConverter())
        .collect(Collectors.toList());
  }

}
