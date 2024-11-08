package nextstep.app;

import nextstep.app.domain.MemberRepository;
import nextstep.app.infrastructure.InmemoryMemberRepository;
import nextstep.app.service.MemberService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.core.userdetails.UserDetailsService;
import nextstep.security.web.DefaultSecurityFilterChain;
import nextstep.security.web.FilterChainProxy;
import nextstep.security.web.SecurityFilterChain;
import nextstep.security.web.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.web.util.matcher.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 시큐리티 객체들을 빈으로 등록해서 의존성 주입으로 조립
 */
@Configuration
public class AppConfig {
    @Bean
    public FilterChainProxy filterChainProxy() {
        return new FilterChainProxy(securityFilterChain());
    }

    //세션을 이렇게 주는게 맞아? FilterChainProxy는 어떻게 가져가는지 보자.
    // SecurityFilter 리스트는 어떻게 가져가지?

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                new RequestMatcher(RequestMatcher.MATCH_ALL, null)
                , Arrays.asList(new UsernamePasswordAuthenticationFilter(authenticationManager())));
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new AuthenticationManager(authenticationProvider());
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new MemberService(memberRepository());
    }

    @Bean
    MemberRepository memberRepository() {
        return new InmemoryMemberRepository();
    }

}
