package nextstep.app;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.AuthenticationProvider;
import nextstep.security.core.authority.GrantedAuthority;
import nextstep.security.core.userdetails.UserDetails;
import nextstep.security.core.userdetails.UserDetailsService;
import nextstep.security.core.userdetails.UsernameNotFoundException;
import nextstep.security.web.DefaultSecurityFilterChain;
import nextstep.security.web.FilterChainProxy;
import nextstep.security.web.SecurityFilterChain;
import nextstep.security.web.authentication.BasicAuthenticationFilter;
import nextstep.security.web.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.web.util.matcher.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

/**
 * 시큐리티 객체들을 빈으로 등록해서 의존성 주입으로 조립
 */
@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final MemberRepository memberRepository;

    @Bean
    public FilterChainProxy filterChainProxy() {
        return new FilterChainProxy(List.of(securityFilterChain()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        return new DefaultSecurityFilterChain(
                new RequestMatcher(RequestMatcher.MATCH_ALL, null)
                , List.of(new UsernamePasswordAuthenticationFilter(authenticationManager())
                , new BasicAuthenticationFilter(authenticationManager())));
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
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

            return new UserDetails() {
                @Override
                public String getUsername() {
                    return member.getName();
                }

                @Override
                public String getPassword() {
                    return member.getPassword();
                }

                @Override
                public Collection<GrantedAuthority> getAuthorities() {
                    return List.of(new GrantedAuthority("USER"));
                }
            };
        };
    }

}
