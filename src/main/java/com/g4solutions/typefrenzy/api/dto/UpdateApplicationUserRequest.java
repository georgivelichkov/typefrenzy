package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateApplicationUserRequest {

  /**
   * Application user name.
   */
  @JsonProperty("username")
  @NotEmpty
  private String username;

  /**
   * Application user email.
   */
  @JsonProperty("email")
  @NotEmpty
  private String email;

  /**
   * Application user password.
   */
  @JsonProperty("password")
  @NotEmpty
  private String password;


}
