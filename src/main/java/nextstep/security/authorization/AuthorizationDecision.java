package nextstep.security.authorization;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorizationDecision {
    private final boolean granted;
}
