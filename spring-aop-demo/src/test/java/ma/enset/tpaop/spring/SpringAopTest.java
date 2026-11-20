package ma.enset.tpaop.spring;

import ma.enset.tpaop.spring.aspects.LoggingAspect;
import ma.enset.tpaop.spring.aspects.SecurityAspect;
import ma.enset.tpaop.spring.config.AppConfig;
import ma.enset.tpaop.spring.metier.IMetier;
import ma.enset.tpaop.spring.security.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validation fonctionnelle des proxys Spring AOP :
 * - Démarrage du contexte IoC et création des proxys dynamiques JDK
 * - Déclenchement de l'aspect de sécurité (@SecuredByAspect)
 * - Déclenchement de l'aspect de journalisation (@Log)
 */
class SpringAopTest {

    private AnnotationConfigApplicationContext context;
    private IMetier metier;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        metier = context.getBean(IMetier.class);
        LoggingAspect.resetMetrics();
        SecurityAspect.resetMetrics();
    }

    @AfterEach
    void tearDown() {
        SecurityContext.reset();
        if (context != null) {
            context.close();
        }
    }

    @Test
    @DisplayName("REQ-SP-01 : Démarrage du contexte et création effective d'un proxy dynamique JDK")
    void testSpringContextLoading() {
        assertNotNull(metier, "Le bean métier doit être injecté");
        assertTrue(java.lang.reflect.Proxy.isProxyClass(metier.getClass()),
                "Spring AOP doit utiliser un proxy dynamique JDK car IMetier est une interface");
    }

    @Test
    @DisplayName("REQ-SP-04 : L'aspect @Log journalise l'exécution et mesure la durée")
    void testLogAspectExecution() {
        SecurityContext.authenticateUser("root", "1234", new String[]{"USER"});

        assertFalse(LoggingAspect.isInvoked());
        metier.process();

        assertTrue(LoggingAspect.isInvoked(), "LoggingAspect doit avoir été invoqué par le proxy");
        assertTrue(LoggingAspect.getLastDuration() >= 0, "La durée d'exécution doit être calculée");
    }

    @Test
    @DisplayName("REQ-SP-05 : SecurityAspect autorise l'accès quand le rôle requis est présent")
    void testSecurityAspectAuthorized() {
        SecurityContext.authenticateUser("root", "1234", new String[]{"ADMIN"});

        assertDoesNotThrow(() -> metier.process());
        double result = metier.compute();
        assertEquals(80.0, result, 0.001);
        assertTrue(SecurityAspect.isCheckPerformed());
    }

    @Test
    @DisplayName("REQ-SP-05 : SecurityAspect bloque l'accès et lève RuntimeException quand le rôle manque")
    void testSecurityAspectUnauthorized() {
        SecurityContext.authenticateUser("root", "1234", new String[]{"USER"});

        // USER a le droit sur process()
        assertDoesNotThrow(() -> metier.process());

        // USER n'a PAS le droit sur compute() (ADMIN requis)
        RuntimeException ex = assertThrows(RuntimeException.class, () -> metier.compute());
        assertEquals("Not Authorized", ex.getMessage());
    }
}
