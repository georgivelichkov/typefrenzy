package com.g4solutions.typefrenzy.service;

import com.g4solutions.typefrenzy.api.error.exceptions.ResourceNotFoundException;
import com.g4solutions.typefrenzy.api.filtering.SpecificationBuilder;
import com.g4solutions.typefrenzy.api.paging.PageableBuilder;
import com.g4solutions.typefrenzy.domain.ApplicationUser;
import com.g4solutions.typefrenzy.domain.Score;
import com.g4solutions.typefrenzy.repository.ScoresRepository;
import com.g4solutions.typefrenzy.util.HttpUtils;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScoresService {

  private final ApplicationUserService applicationUserService;
  private final ScoresRepository scoresRepository;

  /**
   * Creates a new {@link Score}
   *
   * @return the created {@link Score} after it's persisted
   */
  @Transactional
  public Score create(Long wpm, HttpHeaders headers) {
    String authorizationHeader = Optional.ofNullable(headers.get(HttpHeaders.AUTHORIZATION))
        .map(authorizations -> authorizations.get(0))
        .orElse(null);

    if (Objects.nonNull(authorizationHeader)) {

      ApplicationUser user = applicationUserService.showByEmail(
          HttpUtils.getEmailFromRequest(authorizationHeader)
      );

      Score score = Score.create(wpm, user);

      return this.scoresRepository.save(score);
    }

    return null;
  }

  /**
   * Softly deletes a {@link Score} with id, by setting its
   * <code>deletedAt<code/> property
   *
   * @param scoreId score unique identifier
   * @throws ResourceNotFoundException if {@link Score} not found
   */
  @Transactional
  public void delete(String scoreId) {
    scoresRepository.deleteById(scoreId);
  }

  /**
   * Fetches a given {@link Score} by its id.
   *
   * @param scoreId score unique identifier
   * @return {@link Score} if present
   * @throws ResourceNotFoundException if {@link Score} not found
   */
  @Transactional
  public Score show(String scoreId) {
    return this.scoresRepository
        .findById(scoreId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Score with id: %s not found!!!", scoreId))
        );
  }

  /**
   * Fetches a {@link Page} of {@link Score}(s)
   *
   * @return a page with {@link ApplicationUser}(s)
   */
  @Transactional
  public Page<Score> all(Map<String, Object> queryParameters, HttpHeaders headers) {
    return this.scoresRepository.findAll(
        SpecificationBuilder.buildFrom(headers, queryParameters, Score.class),
        PageableBuilder.buildFrom(queryParameters, Score.class,
            Math.toIntExact(this.scoresRepository.count())
        )
    );
  }
}
