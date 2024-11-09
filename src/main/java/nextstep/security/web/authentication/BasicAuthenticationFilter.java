package nextstep.security.web.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BadCredentialsException;
import nextstep.security.context.SecurityContext;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.core.Authentication;
import nextstep.security.core.AuthenticationException;
import nextstep.security.web.util.matcher.RequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends OncePerRequestFilter {
    private final RequestMatcher requiresAuthenticationRequestMatcher = new RequestMatcher("/members", HttpMethod.GET);
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //로그인 경로 요청인지 확인
        if (!requiresAuthenticationRequestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 인증 정보 추출
            Authentication authRequest = convert(request);
            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 인증 시도
            Authentication authResult = authenticationManager.authenticate(authRequest);

            //인증 성공 시
            SecurityContextHolder.setContext(new SecurityContext(authResult));

            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
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
}
