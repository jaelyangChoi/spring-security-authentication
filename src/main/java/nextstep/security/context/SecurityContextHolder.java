package nextstep.security.context;

import org.springframework.util.Assert;

import java.util.function.Supplier;

public class SecurityContextHolder {
    private static final ThreadLocal<Supplier<SecurityContext>> contextHolder = new ThreadLocal<>();

    public static SecurityContext getContext() {
        Supplier<SecurityContext> result = contextHolder.get();
        if (result == null) {
            SecurityContext context = createEmptyContext();
            result = () -> context;
            contextHolder.set(result);
        }

        return result.get();
    }

    public static void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(() -> context); //map.set(this, value);
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }
}

