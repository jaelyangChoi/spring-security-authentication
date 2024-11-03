package nextstep.app.ui;

import lombok.RequiredArgsConstructor;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

}
