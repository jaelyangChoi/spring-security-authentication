package nextstep.security.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import nextstep.security.core.userdetails.UserDetailsService;

import java.util.Objects;

@RequiredArgsConstructor
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

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
            throw new BadCredentialsException("아이디, 패스워드를 확인해주세요");
        }
    }

    private Authentication createSuccessAuthentication(UserDetails user) {
        Authentication authentication = new Authentication(user, null, user.getAuthorities());
        authentication.setAuthenticated(true);
        return authentication;
    }
}
