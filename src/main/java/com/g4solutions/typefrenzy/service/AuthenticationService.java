package com.g4solutions.typefrenzy.service;

import com.g4solutions.typefrenzy.api.dto.CreateApplicationUserRequest;
import com.g4solutions.typefrenzy.api.dto.JwtRequest;
import com.g4solutions.typefrenzy.api.dto.JwtResponse;
import com.g4solutions.typefrenzy.api.error.exceptions.AuthenticationException;
import com.g4solutions.typefrenzy.domain.ApplicationUser;
import com.g4solutions.typefrenzy.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtUserDetailsService userDetailsService;
  private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
  private final ApplicationUserService applicationUserService;

  public JwtResponse authenticate(JwtRequest authenticationRequest) {
    try {
      appAuthentication(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    } catch (Exception ex) {
      log.error("Exception Occurred while trying to authenticate user with email: {}",
          authenticationRequest.getEmail());
      log.error("Error: {}", ex.getMessage());
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(
        authenticationRequest.getEmail()
    );

    return JwtResponse.create(jwtTokenUtil.generateToken(userDetails));
  }

  public JwtResponse register(CreateApplicationUserRequest request) {
    ApplicationUser applicationUser = applicationUserService.create(request);

    try {
      appAuthentication(applicationUser.getEmail(), request.getPassword());
    } catch (Exception ex) {
      log.error("Exception Occurred while trying to authenticate user with email: {}",
          applicationUser.getEmail());
      log.error("Error: {}", ex.getMessage());
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(
        applicationUser.getEmail()
    );

    return JwtResponse.create(jwtTokenUtil.generateToken(userDetails));
  }

  private void appAuthentication(String email, String password) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(email, password));
    } catch (DisabledException e) {
      throw new AuthenticationException(String.format("USER_DISABLED: %s", e));
    } catch (BadCredentialsException e) {
      throw new AuthenticationException(String.format("Invalid credentials: %s", e));
    }
  }

}
