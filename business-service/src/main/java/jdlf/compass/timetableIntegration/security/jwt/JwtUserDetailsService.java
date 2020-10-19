package jdlf.compass.timetableIntegration.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Value("${jwt.username}")
  String username_inmem;

  @Value("${jwt.password}")
  String password;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username_inmem.equals(username)) {
      return new User(
          username,
          "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
          new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
