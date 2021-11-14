package com.g4solutions.typefrenzy.api.controllers;

import com.g4solutions.typefrenzy.api.dto.ApplicationUserResponse;
import com.g4solutions.typefrenzy.api.dto.ApplicationUsersResponse;
import com.g4solutions.typefrenzy.api.dto.CreateApplicationUserRequest;
import com.g4solutions.typefrenzy.api.dto.UpdateApplicationUserRequest;
import com.g4solutions.typefrenzy.service.ApplicationUserService;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
@CrossOrigin
public class ApplicationUserController {

  private final ApplicationUserService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplicationUsersResponse> all(
      @RequestParam Map<String, Object> queryParameters,
      @RequestHeader HttpHeaders headers) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApplicationUsersResponse.create(this.service.all(queryParameters, headers)));
  }

  @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplicationUserResponse> show(
      @PathVariable(name = "userId") String userId) {
    ApplicationUserController.log.info("Requesting information about user with id: {}", userId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApplicationUserResponse.create(this.service.show(userId)));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplicationUserResponse> create(
      @Valid @RequestBody CreateApplicationUserRequest request) {
    ApplicationUserController.log
        .info("Requesting creation of user with the following parameters: {}", request.toString());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApplicationUserResponse.create(this.service.create(request)));
  }

  @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApplicationUserResponse> update(
      @Valid @RequestBody UpdateApplicationUserRequest request,
      @PathVariable(name = "userId") String userId
  ) {
    ApplicationUserController.log
        .info(
            "Requesting update of user for the following userId: {} and with the following properties: {}",
            userId, request.toString());

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApplicationUserResponse.create(this.service.update(request, userId)));
  }


  @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable(name = "userId") String userId) {
    ApplicationUserController.log.info("Requesting deletion of user with id: {}", userId);
    this.service.delete(userId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
