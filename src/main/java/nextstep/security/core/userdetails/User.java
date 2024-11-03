package nextstep.security.core.userdetails;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nextstep.security.core.authority.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
public class User implements UserDetails {
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;


    public static class UserBuilder {
        public UserBuilder roles(String... roles) {
            this.authorities = new HashSet<>();
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"),
                        () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new GrantedAuthority("ROLE_" + role));
            }
            return this;
        }
    }
}
