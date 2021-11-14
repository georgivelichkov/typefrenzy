package com.g4solutions.typefrenzy.util;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HttpUtils {

  private static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

  public static String getJwtTokenFromRequest(String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7);
    } else {
      log.warn("JWT Token does not begin with Bearer String");
      return StringUtils.EMPTY;
    }
  }

  public static String getEmailFromRequest(String authorizationHeader) {
    String email = StringUtils.EMPTY;
    String token = getJwtTokenFromRequest(authorizationHeader);

    try {
      email = jwtTokenUtil.getEmailFromToken(token);
    } catch (IllegalArgumentException e) {
      log.error("Unable to get JWT Token");
    } catch (ExpiredJwtException e) {
      log.error("JWT Token has expired");
    }

    return email;
  }

}
