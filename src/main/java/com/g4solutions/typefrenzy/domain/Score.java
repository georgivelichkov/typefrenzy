package com.g4solutions.typefrenzy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "scores")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
public class Score extends AbstractEntity {

  /**
   * Words per minute
   */
  @Column(name = "wpm", nullable = false)
  private Long wpm;

  /**
   * Application user to whom the score belongs
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  @JsonIgnore
  private ApplicationUser user;

  public static Score create(Long wpm, ApplicationUser user) {
    Score score = new Score();

    score.wpm = wpm;
    score.user = user;

    return score;
  }
}
