package ma.enset.tpaop.aspectj.security;

/**
 * Identifiants fixes pour les démonstrations scriptées et les tests.
 */
public class FixedCredentialsProvider implements CredentialsProvider {

    private final Credentials credentials;

    public FixedCredentialsProvider(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    @Override
    public Credentials get() {
        return credentials;
    }
}
