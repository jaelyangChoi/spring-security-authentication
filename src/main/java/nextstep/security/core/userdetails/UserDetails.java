package nextstep.security.core.userdetails;

import nextstep.security.core.authority.GrantedAuthority;

import java.util.Collection;

public interface UserDetails {

    String getUsername();
    String getPassword();
    Collection<GrantedAuthority> getAuthorities();
}
