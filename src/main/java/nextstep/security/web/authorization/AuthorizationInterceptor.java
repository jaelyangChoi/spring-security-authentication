package nextstep.security.web.authorization;

import nextstep.security.authentication.AuthenticationCredentialsNotFoundException;
import nextstep.security.authorization.AccessDeniedException;
import nextstep.security.authorization.AuthorizationDecision;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.core.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthorizationManager authorizationManager = new AuthorizationManager();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            // 인가 : AuthorizationManger 에게 Authentication 전달하여 권한 확인
            AuthorizationDecision decision = authorizationManager.check(this::getAuthentication, request);

            // 인가 권한이 안맞다면 예외 발생
            if (decision != null && !decision.isGranted()) {
                throw new AccessDeniedException("Unauthorized");
            }

        } catch (AuthenticationCredentialsNotFoundException | AccessDeniedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        return true;
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("Unauthenticated");
        }
        return authentication;
    }
}
