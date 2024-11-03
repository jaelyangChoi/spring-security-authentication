package nextstep.security.core.authority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GrantedAuthority {
    private final String role;
}
