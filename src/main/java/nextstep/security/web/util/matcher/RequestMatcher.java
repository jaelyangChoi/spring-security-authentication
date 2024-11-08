package nextstep.security.web.util.matcher;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Setter
@RequiredArgsConstructor
public class RequestMatcher {

    public static final String MATCH_ALL = "/**";
    private final String url;
    private final HttpMethod httpMethod;


    public boolean matches(HttpServletRequest request) {
        if (this.httpMethod != null && StringUtils.hasText(request.getMethod())
                && this.httpMethod != HttpMethod.valueOf(request.getMethod())) {
            return false;
        }
        return this.url.equals(MATCH_ALL) || url.equals(request.getRequestURI());
    }
}
