package nextstep.security.authentication;

import nextstep.security.core.AuthenticationException;

public class AuthenticationCredentialsNotFoundException extends AuthenticationException {
    public AuthenticationCredentialsNotFoundException(String msg) {
        super(msg);
    }
}
