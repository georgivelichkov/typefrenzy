package com.g4solutions.typefrenzy.domain.factory;


import com.g4solutions.typefrenzy.api.dto.CreateApplicationUserRequest;
import com.g4solutions.typefrenzy.domain.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationUserFactory {

  private final PasswordEncoder passwordEncoder;

  /**
   * Creates a new instance of {@link ApplicationUser} based on createApplicationUserRequest
   *
   * @param createApplicationUserRequest {@link CreateApplicationUserRequest}
   * @return {@link ApplicationUser}
   */
  public ApplicationUser create(CreateApplicationUserRequest createApplicationUserRequest) {
    return ApplicationUser.create(
        createApplicationUserRequest.getUsername(),
        createApplicationUserRequest.getEmail(),
        passwordEncoder.encode(createApplicationUserRequest.getPassword())
    );
  }

}
