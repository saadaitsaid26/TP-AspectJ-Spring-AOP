package ma.enset.tpaop.spring.aspects;

import ma.enset.tpaop.spring.security.SecurityContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect de sécurité RBAC (Role-Based Access Control) conforme aux
 * diapositives 76-78 du cours et à la vidéo 5.
 *
 * <p>Intercepte toute méthode portant {@link SecuredByAspect} et vérifie
 * si l'utilisateur possède l'un des rôles autorisés dans {@link SecurityContext}.</p>
 */
@Component
@Aspect
@Order(1)
public class SecurityAspect {

    private static volatile boolean checkPerformed = false;

    public static boolean isCheckPerformed() {
        return checkPerformed;
    }

    public static void resetMetrics() {
        checkPerformed = false;
    }

    @Around(value = "@annotation(securedByAspect)", argNames = "proceedingJoinPoint,securedByAspect")
    public Object secure(ProceedingJoinPoint proceedingJoinPoint, SecuredByAspect securedByAspect) throws Throwable {
        checkPerformed = true;
        String[] roles = securedByAspect.roles();
        boolean authorized = false;

        for (String r : roles) {
            if (SecurityContext.hasRole(r)) {
                authorized = true;
                break;
            }
        }

        if (!authorized) {
            throw new RuntimeException("Not Authorized");
        }

        return proceedingJoinPoint.proceed();
    }
}
