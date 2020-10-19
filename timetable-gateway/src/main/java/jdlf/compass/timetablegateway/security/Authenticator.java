package com.compass.timetable.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface Authenticator {

     boolean authenticate(ServerHttpRequest request,String serviceId);
}
