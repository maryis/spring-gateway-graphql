package com.compass.timetable.gateway.security.provider;

import com.compass.timetable.gateway.properties.SecurityAuthProperties;
import com.compass.timetable.gateway.security.Credential;
import com.compass.timetable.gateway.security.basic.BasicAuthCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileAuthProvider implements AuthProvider {

  private SecurityAuthProperties properties;

  @Autowired
  public FileAuthProvider(SecurityAuthProperties properties) {
    this.properties = properties;
  }

  @Override
  public Credential getUserInfo(String serviceId, String userId) {
    SecurityAuthProperties.Route routeSecurity = properties.getRoutes().get(serviceId);

    BasicAuthCredentials credential = new BasicAuthCredentials();

    if (routeSecurity != null) {
      String inMemUser = routeSecurity.getUser();
      String inMemPass = routeSecurity.getPassword();

      credential.setUser(inMemUser);
      credential.setPassword(inMemPass);

    }
    return credential;

  }
}
