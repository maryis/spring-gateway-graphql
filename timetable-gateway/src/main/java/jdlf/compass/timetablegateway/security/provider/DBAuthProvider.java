package jdlf.compass.timetablegateway.security.provider;

import jdlf.compass.timetablegateway.security.Credential;
import org.springframework.stereotype.Component;

@Component
public class DBAuthProvider implements AuthProvider {
    @Override
    public Credential getUserInfo(String serviceId, String userId) {
        return null;
    }
}
