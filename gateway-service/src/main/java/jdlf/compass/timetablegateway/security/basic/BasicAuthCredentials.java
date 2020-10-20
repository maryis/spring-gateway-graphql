package jdlf.compass.timetablegateway.security.basic;

import jdlf.compass.timetablegateway.security.Credential;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class BasicAuthCredentials implements Credential {
    private String user;
    private String password;
    private boolean authenticated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicAuthCredentials that = (BasicAuthCredentials) o;
        return user.equals(that.user) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, password);
    }
}
