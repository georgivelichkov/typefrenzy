package com.g4solutions.typefrenzy.service;


import com.g4solutions.typefrenzy.api.dto.CreateApplicationUserRequest;
import com.g4solutions.typefrenzy.api.dto.UpdateApplicationUserRequest;
import com.g4solutions.typefrenzy.api.error.exceptions.ResourceNotFoundException;
import com.g4solutions.typefrenzy.api.filtering.SpecificationBuilder;
import com.g4solutions.typefrenzy.api.paging.PageableBuilder;
import com.g4solutions.typefrenzy.domain.ApplicationUser;
import com.g4solutions.typefrenzy.domain.factory.ApplicationUserFactory;
import com.g4solutions.typefrenzy.repository.ApplicationUserRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

  private final PasswordEncoder passwordEncoder;
  private final ApplicationUserRepository applicationUserRepository;
  private final ApplicationUserFactory applicationUserFactory;


  /**
   * Creates a new {@link ApplicationUser}
   *
   * @param request {@link CreateApplicationUserRequest}
   * @return the created {@link ApplicationUser} after it's persisted
   */
  @Transactional
  public ApplicationUser create(CreateApplicationUserRequest request) {
    ApplicationUser applicationUser = this.applicationUserFactory.create(request);

    if (applicationUserRepository.findByEmail(applicationUser.getEmail()).isPresent()) {
      throw new IllegalArgumentException("User with this email already exists");
    }

    return this.applicationUserRepository.save(applicationUser);
  }

  /**
   * Update a {@link ApplicationUser}
   *
   * @param request {@link UpdateApplicationUserRequest}
   * @param userId application user unique identifier
   * @return the update {@link ApplicationUser} after it's persisted
   */
  @Transactional
  public ApplicationUser update(UpdateApplicationUserRequest request, String userId) {
    ApplicationUser applicationUser = show(userId);

    applicationUser.setUsername(request.getUsername());
    applicationUser.setEmail(request.getEmail());
    applicationUser.setPassword(passwordEncoder.encode(request.getPassword()));

    return this.applicationUserRepository.save(applicationUser);
  }

  /**
   * Softly deletes a {@link ApplicationUser} with id, by setting its
   * <code>deletedAt<code/> property
   *
   * @param userId application user unique identifier
   * @throws ResourceNotFoundException if {@link ApplicationUser} not found
   */
  @Transactional
  public void delete(String userId) {
    Optional<ApplicationUser> applicationUser = this.applicationUserRepository
        .findByIdAndDeletedAtNull(userId);

    if (applicationUser.isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("Application user with id: %s not found!!!", userId));
    }

    applicationUser.ifPresent(user -> {
      user.setDeletedAt(LocalDateTime.now());
      this.applicationUserRepository.save(user);
    });
  }

  /**
   * Fetches a given {@link ApplicationUser} by its id.
   *
   * @param userId application user unique identifier
   * @return {@link ApplicationUser} if present
   * @throws ResourceNotFoundException if {@link ApplicationUser} not found
   */
  @Transactional
  public ApplicationUser show(String userId) {

    return this.applicationUserRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Application user with id: %s not found!!!", userId))
        );
  }

  /**
   * Fetches a given {@link ApplicationUser} by its email.
   *
   * @param email application user's email
   * @return {@link ApplicationUser} if present
   * @throws ResourceNotFoundException if {@link ApplicationUser} not found
   */
  @Transactional
  public ApplicationUser showByEmail(String email) {
    return this.applicationUserRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Application user with email: %s not found!!!", email))
        );
  }

  /**
   * Fetches a {@link Page} of {@link ApplicationUser}(s)
   *
   * @return a page with {@link ApplicationUser}(s)
   */
  @Transactional
  public Page<ApplicationUser> all(Map<String, Object> queryParameters, HttpHeaders headers) {
    return this.applicationUserRepository.findAll(
        SpecificationBuilder.buildFrom(headers, queryParameters, ApplicationUser.class),
        PageableBuilder.buildFrom(queryParameters, ApplicationUser.class,
            Math.toIntExact(this.applicationUserRepository.count())
        )
    );
  }
}
