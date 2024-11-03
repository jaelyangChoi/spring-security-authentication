package nextstep.security.web.authentication;

import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BadCredentialsException;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthenticationInterceptor implements HandlerInterceptor {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationInterceptor() {
        this.authenticationManager = new AuthenticationManager();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authRequest;
        Authentication authResponse;

        // 인증 정보 추출
        try {
            authRequest = convert(request);
        } catch (BadCredentialsException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }

        if (authRequest == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        //인증이 필요할 경우 인증
        if (authenticationIsRequired((String) authRequest.getPrincipal())) {

            // 인증
            try {
                authResponse = authenticationManager.authenticate(authRequest);
            } catch (AuthenticationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return false;
            }

            //인증 정보 저장
            SecurityContextHolder.setContext(new SecurityContext(authResponse));

            // 인증 정보를 세션에 저장
            UserDetails principal = (UserDetails) authResponse.getPrincipal();
            request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, principal);
        }

        return true; //다음 인터셉터로
    }


    private Authentication convert(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        //정상 요청 여부 확인
        if (header == null) return null;
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, "Basic")) return null;
        if (header.equalsIgnoreCase("Basic"))
            throw new BadCredentialsException("Empty basic authentication token");

        //BASE64 decoding and parsing
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Token);
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }

        return new Authentication(token.substring(0, delim), token.substring(delim + 1), null);
    }

    private boolean authenticationIsRequired(String username) {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        return existingAuth == null || !existingAuth.getName().equals(username) || !existingAuth.isAuthenticated(); //모두 불만족하면 true
    }

}
//1. Basic 인증을 사용하여 사용자를 식별한다. 요청의 Authorization 헤더에서 Basic 인증 정보를 추출
//2. 인증 성공 시 Session 을 사용하여 인증 정보를 저장한다.
//3. 인가 필터로 -> Member 로 등록되어있는 사용자만 가능하도록 한다