package nextstep.security.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;

@RequiredArgsConstructor
public class AuthenticationManager {
    private final AuthenticationProvider authenticationProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        return authenticationProvider.authenticate(authentication);
    }
}
