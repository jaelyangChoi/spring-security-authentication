package nextstep.security.authentication;

import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;

public class AuthenticationManager {
    private final AuthenticationProvider authenticationProvider = new AuthenticationProvider();

    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        return authenticationProvider.authenticate(authentication);
    }
}
