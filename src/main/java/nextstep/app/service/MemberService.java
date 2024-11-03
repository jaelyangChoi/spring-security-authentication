package nextstep.app.service;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.core.userdetails.User;
import nextstep.security.core.userdetails.UserDetails;
import nextstep.security.core.userdetails.UserDetailsService;
import nextstep.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles("MEMBER")
                .build();
    }
}
