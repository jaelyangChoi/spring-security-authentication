package nextstep.security.authentication;

import nextstep.app.ApplicationContextProvider;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import nextstep.security.core.userdetails.UserDetailsService;

import java.util.Objects;

public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public AuthenticationProvider() {
        this.userDetailsService = ApplicationContextProvider.getApplicationContext().getBean(UserDetailsService.class);
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 사용자 정보 load
        UserDetails loadedUser = retrieveUser(authentication.getPrincipal());

        // 패스워드 체크
        authenticationChecks(authentication, loadedUser);

        // 인증 정보 생성
        return createSuccessAuthentication(loadedUser);
    }


    private UserDetails retrieveUser(Object username) {
        return userDetailsService.loadUserByUsername((String) username);
    }

    private void authenticationChecks(Authentication authentication, UserDetails loadedUser) {
        if (!Objects.equals(authentication.getCredentials().toString(), loadedUser.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    private Authentication createSuccessAuthentication(UserDetails user) {
        Authentication authentication = new Authentication(user, null, user.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }
}
