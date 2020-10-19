package com.compass.timetable.gateway.security.provider;

import com.compass.timetable.gateway.security.Credential;

public interface AuthProvider {

    Credential getUserInfo(String serviceId, String userId);
}
