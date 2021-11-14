package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.g4solutions.typefrenzy.domain.ApplicationUser;
import com.g4solutions.typefrenzy.domain.Score;
import com.g4solutions.typefrenzy.util.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonPropertyOrder({"id", "createdAt", "updatedAt", "deletedAt"})
@ToString
public class ApplicationUserResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("createdAt")
  @JsonFormat(pattern = DateTimeFormat.PATTERN)
  private LocalDateTime createdAt;

  @JsonProperty("updatedAt")
  @JsonFormat(pattern = DateTimeFormat.PATTERN)
  private LocalDateTime updatedAt;

  @JsonProperty("deletedAt")
  @JsonFormat(pattern = DateTimeFormat.PATTERN)
  private LocalDateTime deletedAt;

  @JsonProperty("username")
  private String username;

  @JsonProperty("email")
  private String email;

  @JsonProperty("scores")
  private Set<Score> scores;


  public static ApplicationUserResponse create(ApplicationUser applicationUser) {
    ApplicationUserResponse response = new ApplicationUserResponse();

    response.id = applicationUser.getId();
    response.createdAt = applicationUser.getCreatedAt();
    response.updatedAt = applicationUser.getUpdatedAt();
    response.deletedAt = applicationUser.getDeletedAt();
    response.email = applicationUser.getEmail();
    response.username = applicationUser.getUsername();
    response.scores = applicationUser.getScores();

    return response;
  }

}
