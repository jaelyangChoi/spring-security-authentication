package nextstep.security.web;

import lombok.RequiredArgsConstructor;
import nextstep.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
public class DefaultSecurityFilterChain implements SecurityFilterChain {
    private final RequestMatcher requestMatcher;
    private final List<Filter> filters;

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }
}
