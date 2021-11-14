package com.g4solutions.typefrenzy.service;

import com.g4solutions.typefrenzy.domain.ApplicationUser;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private ApplicationUserService applicationUserService;

  @Autowired
  public void setApplicationUserService(
      ApplicationUserService applicationUserService) {
    this.applicationUserService = applicationUserService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    ApplicationUser applicationUser = applicationUserService.showByEmail(email);

    return new User(applicationUser.getEmail(), applicationUser.getPassword(),
        Collections.emptyList());
  }
}

