package ma.enset.tpaop.spring;

import ma.enset.tpaop.spring.security.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires de la gestion du contexte de sécurité applicatif.
 */
class SecurityContextTest {

    @AfterEach
    void tearDown() {
        SecurityContext.reset();
    }

    @Test
    @DisplayName("REQ-SP-03 : Authentification réussie avec 'root' / '1234'")
    void testAuthenticationSuccessAndFailure() {
        assertFalse(SecurityContext.hasRole("ADMIN"));

        SecurityContext.authenticateUser("root", "1234", new String[]{"ADMIN", "USER"});

        assertEquals("root", SecurityContext.getUsername());
        assertTrue(SecurityContext.hasRole("ADMIN"));
        assertTrue(SecurityContext.hasRole("USER"));
        assertFalse(SecurityContext.hasRole("MANAGER"));
    }

    @Test
    @DisplayName("REQ-SP-03 : Rejet des identifiants incorrects")
    void testAuthenticationFailure() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            SecurityContext.authenticateUser("unknown", "0000", new String[]{"ADMIN"});
        });

        assertEquals("Invalid Credentials..", ex.getMessage());
        assertEquals("", SecurityContext.getUsername());
        assertFalse(SecurityContext.hasRole("ADMIN"));
    }
}
