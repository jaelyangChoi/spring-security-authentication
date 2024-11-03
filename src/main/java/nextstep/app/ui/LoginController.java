package nextstep.app.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletRequest request, HttpSession session) {
        log.info("Login request received");
        return ResponseEntity.ok().build();
    }
}
