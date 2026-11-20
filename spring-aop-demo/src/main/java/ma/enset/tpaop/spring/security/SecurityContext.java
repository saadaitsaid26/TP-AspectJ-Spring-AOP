package ma.enset.tpaop.spring.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Gestionnaire de contexte d'authentification et de sécurité en mémoire,
 * reproduisant la classe 'ApplicationContext' présentée dans le cours (diapositive 67).
 *
 * <p>Renommé en {@code SecurityContext} pour éviter toute ambiguïté avec
 * {@link org.springframework.context.ApplicationContext}, avec un alias
 * disponible pour fidélité pédagogique.</p>
 */
public final class SecurityContext {

    private static String username = "";
    private static String password = "";
    private static Set<String> roles = new HashSet<>();

    private SecurityContext() {
    }

    /**
     * Authentification de l'utilisateur avec ses rôles.
     * Les identifiants de référence du cours sont "root" / "1234".
     */
    public static synchronized void authenticateUser(String u, String p, String[] inputRoles) {
        if ("root".equals(u) && "1234".equals(p)) {
            username = u;
            password = p;
            roles = new HashSet<>(Arrays.asList(inputRoles));
        } else {
            reset();
            throw new RuntimeException("Invalid Credentials..");
        }
    }

    /**
     * Vérification de l'attribution d'un rôle donné à l'utilisateur courant.
     */
    public static synchronized boolean hasRole(String role) {
        return roles.contains(role);
    }

    /**
     * Réinitialisation de la session de sécurité.
     */
    public static synchronized void reset() {
        username = "";
        password = "";
        roles.clear();
    }

    public static synchronized String getUsername() {
        return username;
    }

    public static synchronized Set<String> getRoles() {
        return new HashSet<>(roles);
    }
}
