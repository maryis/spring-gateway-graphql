package jdlf.compass.timetablegateway.security.provider;

import jdlf.compass.timetablegateway.properties.SecurityAuthProperties;
import jdlf.compass.timetablegateway.security.Credential;
import jdlf.compass.timetablegateway.security.basic.BasicAuthCredentials;
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
