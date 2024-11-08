package nextstep.security.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityContextRepository {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public SecurityContext containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return null;
        return (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
}
