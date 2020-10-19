package jdlf.compass.timetablegateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface Authenticator {

     boolean authenticate(ServerHttpRequest request,String serviceId);
}
