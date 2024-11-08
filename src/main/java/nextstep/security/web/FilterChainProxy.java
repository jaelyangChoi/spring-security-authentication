package nextstep.security.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * SecurityFilterChain 에 포함된 Security Filter 들을 하나씩 실행한다.
 */
@RequiredArgsConstructor
public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) servletRequest);

        if (filters == null || filters.isEmpty())
            return;

        VirtualFilterChain virtualFilterChain = new VirtualFilterChain(filterChain, filters);
        virtualFilterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Returns the first filter chain matching the supplied URL.
     */
    private List<Filter> getFilters(HttpServletRequest request) {
        for (SecurityFilterChain chain : filterChains) {
            if (chain.matches(request))  /*TODO 일단 모두 true return */
                return chain.getFilters();
        }
        return null;
    }

    private static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            if (this.currentPosition == this.size) {
                this.originalChain.doFilter(request, response);
                return;
            }

            Filter nextFilter = this.additionalFilters.get(this.currentPosition++);
            nextFilter.doFilter(request, response, this); //안에서 this로 넘긴 filterChian의 doFilter를 호출해서 이어진다.
        }
    }
}
