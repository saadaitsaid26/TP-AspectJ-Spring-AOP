package ma.enset.tpaop.aspectj.security;

/**
 * Source des identifiants utilisée par l'aspect de sécurité. Cette
 * indirection permet de conserver la démonstration interactive du cours
 * tout en rendant les scénarios automatisés reproductibles.
 */
@FunctionalInterface
public interface CredentialsProvider {

    Credentials get();
}
