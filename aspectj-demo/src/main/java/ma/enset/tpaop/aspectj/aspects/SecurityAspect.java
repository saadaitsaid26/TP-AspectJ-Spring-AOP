package ma.enset.tpaop.aspectj.aspects;

import ma.enset.tpaop.aspectj.security.ConsoleCredentialsProvider;
import ma.enset.tpaop.aspectj.security.Credentials;
import ma.enset.tpaop.aspectj.security.CredentialsProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Objects;

/**
 * Authentification autour de l'appel à la méthode de démarrage de
 * l'application. Le pointcut utilise volontairement {@code call} : le
 * contexte est celui de l'appelant, comme expliqué dans le cours. Ce
 * pointcut n'est pas supporté par Spring AOP, qui ne voit que les
 * exécutions de méthodes.
 */
@Aspect
public class SecurityAspect {

    private static volatile CredentialsProvider credentialsProvider =
            new ConsoleCredentialsProvider();

    public static void setCredentialsProvider(CredentialsProvider provider) {
        credentialsProvider = Objects.requireNonNull(provider, "provider");
    }

    public static void resetCredentialsProvider() {
        credentialsProvider = new ConsoleCredentialsProvider();
    }

    @Around("call(* ma.enset.tpaop.aspectj.ui.BankApplication.start(..))")
    public Object secureApp(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Credentials credentials = credentialsProvider.get();

        System.out.println("Authentication");
        if ("root".equals(credentials.username()) && "123".equals(credentials.password())) {
            System.out.println("Before starting");
            try {
                return proceedingJoinPoint.proceed();
            } finally {
                System.out.println("End of Application");
            }
        }
        System.out.println("Access denied ...");
        return null;
    }
}
