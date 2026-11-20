package ma.enset.tpaop.aspectj.security;

/**
 * Identifiants fournis au aspect de sécurité.
 */
public record Credentials(String username, String password) {
}
