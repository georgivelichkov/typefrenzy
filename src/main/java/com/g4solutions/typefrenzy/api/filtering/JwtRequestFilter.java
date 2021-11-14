package com.g4solutions.typefrenzy.api.filtering;

import com.g4solutions.typefrenzy.service.JwtUserDetailsService;
import com.g4solutions.typefrenzy.util.HttpUtils;
import com.g4solutions.typefrenzy.util.JwtTokenUtil;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private JwtUserDetailsService jwtUserDetailsService;

  @Autowired
  public void setJwtUserDetailsService(
      JwtUserDetailsService jwtUserDetailsService) {
    this.jwtUserDetailsService = jwtUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws ServletException, IOException {
    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    String email = null;
    String jwtToken = null;

    if (Objects.nonNull(authorization)) {
      email = HttpUtils.getEmailFromRequest(authorization);
      jwtToken = HttpUtils.getJwtTokenFromRequest(authorization);
    }

    // Once we get the token validate it.
    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      logger.info("Unauthenticated");
      UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(email);

      // if token is valid configure Spring Security to manually set
      // authentication
      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }

}