package nextstep.app;

import nextstep.security.web.authentication.BasicAuthenticationInterceptor;
import nextstep.security.web.authentication.UsernamePasswordAuthenticationInterceptor;
import nextstep.security.web.authorization.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor()).addPathPatterns("/login");
        // /members 경로는 인증 후 인가 처리
        registry.addInterceptor(new BasicAuthenticationInterceptor()).addPathPatterns("/members");
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/members");
    }
}
