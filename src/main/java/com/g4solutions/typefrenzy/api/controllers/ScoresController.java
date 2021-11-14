package com.g4solutions.typefrenzy.api.controllers;

import com.g4solutions.typefrenzy.api.dto.ScoreResponse;
import com.g4solutions.typefrenzy.api.dto.ScoresResponse;
import com.g4solutions.typefrenzy.service.ScoresService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scores")
@Slf4j
@CrossOrigin
public class ScoresController {

  private final ScoresService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ScoresResponse> all(
      @RequestParam Map<String, Object> queryParameters,
      @RequestHeader HttpHeaders headers) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(ScoresResponse.create(this.service.all(queryParameters, headers)));
  }

  @GetMapping(value = "/{scoreId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ScoreResponse> show(
      @PathVariable(name = "scoreId") String scoreId) {
    ScoresController.log.info("Requesting information about score with id: {}", scoreId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(ScoreResponse.create(this.service.show(scoreId)));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ScoreResponse> create(
      @RequestBody Map<String, Long> request,
      @RequestHeader HttpHeaders headers) {
    ScoresController.log
        .info("Requesting creation of score with the following parameters: {}", request.toString());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ScoreResponse.create(service.create(request.get("wpm"), headers)));
  }


  @DeleteMapping(value = "/{scoreId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable(name = "scoreId") String scoreId) {
    ScoresController.log.info("Requesting deletion of score with id: {}", scoreId);
    this.service.delete(scoreId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
