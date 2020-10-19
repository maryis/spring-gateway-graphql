package com.compass.timetable.gateway.security.provider;

import com.compass.timetable.gateway.security.Credential;
import org.springframework.stereotype.Component;

@Component
public class DBAuthProvider implements AuthProvider {
    @Override
    public Credential getUserInfo(String serviceId, String userId) {
        return null;
    }
}
