package com.g4solutions.typefrenzy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  @JsonProperty("token")
  private String token;


  public static JwtResponse create(String token) {
    JwtResponse response = new JwtResponse();

    response.token = token;

    return response;
  }

}