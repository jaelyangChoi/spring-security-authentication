package nextstep.security.web.authentication;

import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager = new AuthenticationManager();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authRequest = (Authentication) request.getAttribute("authRequest");
        Authentication authResponse;

        try {
            // 인증
            authResponse = authenticationManager.authenticate(authRequest);

            //인증 정보 저장
            SecurityContextHolder.setContext(new SecurityContext(authResponse));

        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        // 인증 정보를 세션에 저장
        UserDetails principal = (UserDetails) authResponse.getPrincipal();
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, principal);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
    }
}
