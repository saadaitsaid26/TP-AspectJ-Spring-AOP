package ma.enset.tpaop.spring.aspects;

import ma.enset.tpaop.spring.security.SecurityContext;

/**
 * Alias de compatibilité pédagogique exacte avec le code des diapositives
 * 67-72 de cours, où la classe statique de sécurité était nommée 'ApplicationContext'.
 */
public final class ApplicationContext {

    private ApplicationContext() {
    }

    public static void authenticateUser(String u, String p, String[] inputRoles) {
        SecurityContext.authenticateUser(u, p, inputRoles);
    }

    public static boolean hasRole(String role) {
        return SecurityContext.hasRole(role);
    }

    public static void reset() {
        SecurityContext.reset();
    }
}
