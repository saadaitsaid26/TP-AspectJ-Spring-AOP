package ma.enset.tpaop.spring.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Aspect de journalisation et de mesure de temps d'exécution (cours p. 73 et vidéo 5).
 * Intercepte les méthodes marquées par l'annotation {@link Log}.
 */
@Component
@Aspect
@Order(2)
public class LoggingAspect {

    private static volatile boolean invoked = false;
    private static volatile long lastDuration = -1;

    public static boolean isInvoked() {
        return invoked;
    }

    public static long getLastDuration() {
        return lastDuration;
    }

    public static void resetMetrics() {
        invoked = false;
        lastDuration = -1;
    }

    @Around("@annotation(ma.enset.tpaop.spring.aspects.Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        invoked = true;
        Date d1 = new Date();
        System.out.println("Before ...." + d1);

        try {
            Object result = joinPoint.proceed();
            Date d2 = new Date();
            lastDuration = d2.getTime() - d1.getTime();
            System.out.println("After .... " + d2);
            System.out.println("Execution Duration : " + lastDuration + " ms");
            return result;
        } catch (Throwable throwable) {
            Date d2 = new Date();
            lastDuration = d2.getTime() - d1.getTime();
            System.out.println("After (Exception) .... " + d2 + " : " + throwable.getMessage());
            throw throwable;
        }
    }
}
