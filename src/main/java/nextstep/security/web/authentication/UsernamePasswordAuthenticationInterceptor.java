package nextstep.security.web.authentication;

import lombok.extern.slf4j.Slf4j;
import nextstep.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class UsernamePasswordAuthenticationInterceptor extends AuthenticationInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // request parameter 로 전달된 정보를 인증 정보로 변환
        Authentication authRequest = new Authentication(request.getParameter("username"), request.getParameter("password"), null);

        // 인증
        request.setAttribute("authRequest", authRequest);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}