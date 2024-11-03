package nextstep.security.authorization;

import nextstep.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

public class AuthorizationManager {
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        boolean granted = isGranted(authentication.get());
        return new AuthorizationDecision(granted);
    }

    private boolean isGranted(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
