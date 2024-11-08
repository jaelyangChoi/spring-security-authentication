package nextstep.security.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityContextRepository {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private String springSecurityContextKey = SPRING_SECURITY_CONTEXT_KEY;

    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(springSecurityContextKey, context);
    }

    public boolean containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        return session.getAttribute(this.springSecurityContextKey) != null;
    }
}
