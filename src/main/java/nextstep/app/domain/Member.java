package nextstep.app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Member {
    private final String email;
    private final String password;
    private final String name;
    private final String imageUrl;

    public Member(String email, String password, String name, String imageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
