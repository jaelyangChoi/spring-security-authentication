package nextstep.security.core.userdetails;

import javax.naming.AuthenticationException;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
