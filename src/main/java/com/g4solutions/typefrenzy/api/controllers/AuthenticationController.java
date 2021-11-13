package com.g4solutions.typefrenzy.api.controllers;

import com.g4solutions.typefrenzy.api.dto.CreateApplicationUserRequest;
import com.g4solutions.typefrenzy.api.dto.JwtRequest;
import com.g4solutions.typefrenzy.api.dto.JwtResponse;
import com.g4solutions.typefrenzy.service.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
public class AuthenticationController {

  private final AuthenticationService authService;

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequest authenticationRequest) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(authService.authenticate(authenticationRequest));
  }

  @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponse> register(
      @Valid @RequestBody CreateApplicationUserRequest createApplicationUserRequest) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(authService.register(createApplicationUserRequest));
  }
}
