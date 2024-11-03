package nextstep.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"nextstep.app", "nextstep.util"})
public class SecurityAuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthenticationApplication.class, args);
    }
}
