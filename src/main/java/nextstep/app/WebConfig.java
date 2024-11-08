package nextstep.app;

import lombok.RequiredArgsConstructor;
import nextstep.security.web.FilterChainProxy;
import nextstep.web.filter.DelegatingFilterProxy;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FilterChainProxy filterChainProxy;

    @Bean
    public FilterRegistrationBean<Filter> delegatingFilterProxy() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new DelegatingFilterProxy(filterChainProxy));
        filterFilterRegistrationBean.addUrlPatterns("/*");
        return filterFilterRegistrationBean;
    }
}
