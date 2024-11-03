package nextstep.security.core.userdetails;

import nextstep.security.core.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {
    public UsernameNotFoundException(String msg) {
        super(msg);
    }
    public UsernameNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
