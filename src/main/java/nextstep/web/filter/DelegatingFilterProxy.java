package nextstep.web.filter;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;

/**
 * 서블릿 필터 처리를 스프링에 들어있는 빈으로 위임하고 싶을 때 사용하는 서블릿 필터
 * FilterChainProxy 에게 시큐리티 관련 처리를 위임
 */

public class DelegatingFilterProxy extends GenericFilterBean {

    private volatile Filter delegate;

    public DelegatingFilterProxy(Filter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Let the delegate perform the actual doFilter operation.
        invokeDelegate(delegate, servletRequest, servletResponse, filterChain);
    }

    protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        delegate.doFilter(request, response, filterChain);
    }
}
