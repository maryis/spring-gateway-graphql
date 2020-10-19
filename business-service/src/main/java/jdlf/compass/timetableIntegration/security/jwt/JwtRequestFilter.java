package jdlf.compass.timetableIntegration.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jdlf.compass.timetableIntegration.common.JwtTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired private JwtUserDetailsService jwtUserDetailsService;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
      throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;
    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

      jwtToken = requestTokenHeader.substring(7);

      try {
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);

      } catch (IllegalArgumentException e) {

        throw new BadCredentialsException("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {

        //                throw new TokenExpireException("JWT Token has expired");
        throw new AccountExpiredException("JWT Token has expired");
      }
    } else {
      //
    }

    // Once we get the token validate it.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

      // if token is valid configure Spring Security to manually set
      // authentication
      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    chain.doFilter(request, response);
  }
}
