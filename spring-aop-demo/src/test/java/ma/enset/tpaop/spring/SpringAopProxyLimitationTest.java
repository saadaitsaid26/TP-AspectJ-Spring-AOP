package ma.enset.tpaop.spring;

import ma.enset.tpaop.spring.aspects.LoggingAspect;
import ma.enset.tpaop.spring.config.AppConfig;
import ma.enset.tpaop.spring.metier.IMetier;
import ma.enset.tpaop.spring.security.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validation de la différence architecturale capitale entre AspectJ et Spring AOP :
 * Dans Spring AOP (architecture basée sur des proxys), les invocations internes
 * (self-invocation via 'this') n'empruntent pas le proxy et échappent donc aux aspects.
 * Ce test prouve et documente cette limitation technique exigée par les consignes (§7).
 */
class SpringAopProxyLimitationTest {

    private AnnotationConfigApplicationContext context;
    private IMetier metier;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        metier = context.getBean(IMetier.class);
        LoggingAspect.resetMetrics();
        SecurityContext.authenticateUser("root", "1234", new String[]{"ADMIN"});
    }

    @AfterEach
    void tearDown() {
        SecurityContext.reset();
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("REQ-SP-07 : Démonstration de la limitation de self-invocation avec Spring AOP")
    void testInternalCallBypassesProxyAspect() {
        // Cas 1 : Appel externe direct de process() via la référence de proxy
        LoggingAspect.resetMetrics();
        metier.process();
        assertTrue(LoggingAspect.isInvoked(),
                "Lors d'un appel externe direct, le proxy intercepte et déclenche LoggingAspect");

        // Cas 2 : Appel de internalCallDemo() qui invoque en interne this.process()
        // Comme internalCallDemo n'est pas annoté @Log, et que this.process() est un appel interne direct,
        // LoggingAspect ne doit PAS être déclenché lors de l'appel interne !
        LoggingAspect.resetMetrics();
        metier.internalCallDemo();
        assertFalse(LoggingAspect.isInvoked(),
                "Lors d'un appel interne (self-invocation), le proxy est contourné : LoggingAspect N'EST PAS déclenché");
    }
}
