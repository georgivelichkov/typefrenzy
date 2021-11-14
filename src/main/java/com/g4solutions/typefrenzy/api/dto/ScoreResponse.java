package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.g4solutions.typefrenzy.domain.Score;
import com.g4solutions.typefrenzy.util.DateTimeFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonPropertyOrder({"id", "createdAt", "updatedAt", "deletedAt"})
@ToString
public class ScoreResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("createdAt")
  @JsonFormat(pattern = DateTimeFormat.PATTERN)
  private LocalDateTime createdAt;

  @JsonProperty("updatedAt")
  @JsonFormat(pattern = DateTimeFormat.PATTERN)
  private LocalDateTime updatedAt;

  @JsonProperty("wpm")
  private Long wpm;

  @JsonProperty("user_id")
  private String userId;


  public static ScoreResponse create(Score score) {
    ScoreResponse response = new ScoreResponse();

    response.id = score.getId();
    response.createdAt = score.getCreatedAt();
    response.updatedAt = score.getUpdatedAt();
    response.wpm = score.getWpm();
    response.userId = score.getUser().getId();

    return response;
  }

}
