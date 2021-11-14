package com.g4solutions.typefrenzy.api.dto;

import com.g4solutions.typefrenzy.domain.Score;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public class ScoresResponse extends
    PageableResponse<Score, ScoreResponse> {

  private ScoresResponse(Page<Score> page) {
    super(page);
  }

  public static ScoresResponse create(Page<Score> scorePage) {
    return new ScoresResponse(scorePage);
  }

  @Override
  public Function<Score, ScoreResponse> responseConverter() {
    return ScoreResponse::create;
  }
}
