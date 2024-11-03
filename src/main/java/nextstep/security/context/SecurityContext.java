package nextstep.security.context;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.security.core.Authentication;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SecurityContext {
    private Authentication authentication;
}
