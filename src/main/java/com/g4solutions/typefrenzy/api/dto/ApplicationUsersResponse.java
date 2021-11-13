package com.g4solutions.typefrenzy.api.dto;

import com.g4solutions.typefrenzy.domain.ApplicationUser;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public class ApplicationUsersResponse extends
    PageableResponse<ApplicationUser, ApplicationUserResponse> {

  private ApplicationUsersResponse(Page<ApplicationUser> page) {
    super(page);
  }

  public static ApplicationUsersResponse create(Page<ApplicationUser> applicationUserPage) {
    return new ApplicationUsersResponse(applicationUserPage);
  }

  @Override
  public Function<ApplicationUser, ApplicationUserResponse> responseConverter() {
    return ApplicationUserResponse::create;
  }
}
