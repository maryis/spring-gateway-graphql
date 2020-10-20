package jdlf.compass.timetablegateway.security.basic;

import jdlf.compass.timetablegateway.exception.BadCredentialsException;
import jdlf.compass.timetablegateway.security.Authenticator;
import jdlf.compass.timetablegateway.security.provider.AuthProvider;
import jdlf.compass.timetablegateway.util.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class BasicAuthenticator implements Authenticator {

    public static final String AUTH_CREDENTIALS = "AUTH_CREDENTIALS";
    public static final String PREFIX_BASIC = "Basic ";

    private Base64Encoder encoder;
    private AuthProvider provider;

    @Autowired
    public BasicAuthenticator(Base64Encoder encoder,@Qualifier("fileAuthProvider") AuthProvider provider) {
        this.encoder = encoder;
        this.provider = provider;
    }

    @Override
    public boolean authenticate(ServerHttpRequest request,String serviceId) {

        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (headers == null || headers.isEmpty()) {
            throw new BadCredentialsException("Authorization header is missing!");
        }

        String authorization = headers.get(0);

        if (StringUtils.isEmpty(authorization)) {
            throw new BadCredentialsException("No Credentials Provided");
        }

        if (!authorization.startsWith(PREFIX_BASIC)) {
            throw new BadCredentialsException("Invalid Authorization header provided");
        }

        String[] authSegments = encoder.decode(authorization.substring(PREFIX_BASIC.length())).split(":");

        BasicAuthCredentials requestCredentials = new BasicAuthCredentials();
        requestCredentials.setUser(authSegments[0]);
        requestCredentials.setPassword(authSegments[1]);

        BasicAuthCredentials realCredentials = (BasicAuthCredentials) provider.getUserInfo(serviceId,"");

        if(realCredentials.equals(requestCredentials))
            return true;

        return false;

    }
}
