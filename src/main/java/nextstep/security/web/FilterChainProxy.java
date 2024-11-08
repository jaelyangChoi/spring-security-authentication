package nextstep.security.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * SecurityFilterChain 에 포함된 Security Filter 들을 하나씩 실행한다.
 */
@Slf4j
public class FilterChainProxy extends GenericFilterBean {

    private final List<SecurityFilterChain> filterChains;

    public FilterChainProxy(SecurityFilterChain filterChain) {
        this.filterChains = Arrays.asList(filterChain);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        List<Filter> filters = getFilters(request);
        if (filters == null || filters.isEmpty()) {
            return;
        }
        new VirtualFilterChain(filterChain, filters).doFilter(request, response);
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
            log.info("nextFilter = {}", nextFilter.getClass().getName());
            nextFilter.doFilter(request, response, this); //안에서 this로 넘긴 filterChian의 doFilter를 호출해서 이어진다.
        }
    }
}
