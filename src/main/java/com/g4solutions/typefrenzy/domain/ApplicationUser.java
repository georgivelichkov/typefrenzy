package com.g4solutions.typefrenzy.domain;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationUser extends AbstractEntity {

  /**
   * Application username.
   */
  @Column(name = "username", nullable = false)
  private String username;

  /**
   * Application user email.
   */
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  /**
   * Application user password.
   */
  @Column(name = "password", nullable = false)
  private String password;

  /**
   * Deleted date and time. Used for soft deleted objects.
   */
  @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime deletedAt;

  /**
   * Application user scores.
   */
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Set<Score> scores = new LinkedHashSet<>();

  /**
   * Creates an instance of {@link ApplicationUser}
   *
   * @param username username of the user
   * @param email email of the user
   * @param password password of the user
   * @return generated instance for {@link ApplicationUser}
   */
  public static ApplicationUser create(String username, String email, String password) {
    ApplicationUser applicationUser = new ApplicationUser();

    applicationUser.username = username;
    applicationUser.email = email;
    applicationUser.password = password;

    return applicationUser;
  }

}
