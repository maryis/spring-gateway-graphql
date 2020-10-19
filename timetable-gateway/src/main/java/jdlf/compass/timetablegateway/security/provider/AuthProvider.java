package jdlf.compass.timetablegateway.security.provider;

import jdlf.compass.timetablegateway.security.Credential;

public interface AuthProvider {

    Credential getUserInfo(String serviceId, String userId);
}
