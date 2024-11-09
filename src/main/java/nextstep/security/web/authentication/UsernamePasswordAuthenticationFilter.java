package nextstep.security.web.authentication;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.context.SecurityContextRepository;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.web.util.matcher.RequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter extends GenericFilterBean {
    private final RequestMatcher requiresAuthenticationRequestMatcher = new RequestMatcher("/login", HttpMethod.POST);
    private String forwardUrl = "/";
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new SecurityContextRepository();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }


    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //로그인 경로 요청인지 확인
        if (!requiresAuthenticationRequestMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        //로그인 과정 시도
        try {
            //인증 시도
            Authentication authenticationResult = attemptAuthentication(request);
            if (authenticationResult == null) {
                throw new AuthenticationException("Authentication failed");
            }

            //로그인 성공 핸들러
            successfulAuthentication(request, response, authenticationResult);
        }
        // Authentication failed
        catch (AuthenticationException e) {
            unsuccessfulAuthentication(response);
        }
    }


    private Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        Authentication authRequest = Authentication.unauthenticated(request.getParameter("username"), request.getParameter("password"));
        return authenticationManager.authenticate(authRequest);
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authenticationResult) throws ServletException, IOException {
        SecurityContext context = new SecurityContext(authenticationResult);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request);
        //this.successHandler.onAuthenticationSuccess(request, response, authResult);
        //성공 시 인덱스 화면으로 포워딩
        request.getRequestDispatcher(this.forwardUrl).forward(request, response);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
