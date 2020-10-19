package jdlf.compass.timetableIntegration.exeption;

import org.springframework.security.access.AccessDeniedException;

public class TokenExpireException extends AccessDeniedException {

  public TokenExpireException(String msg) {
    super(msg);
  }
}
