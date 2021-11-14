package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateScoreRequest {

  /**
   * Words Per Minute.
   */
  @JsonProperty("wpm")
  private Long wpm;


}
