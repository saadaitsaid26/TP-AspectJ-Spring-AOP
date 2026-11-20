package ma.enset.tpaop.aspectj.security;

import java.util.Scanner;

/**
 * Saisie interactive des identifiants sur la console.
 */
public class ConsoleCredentialsProvider implements CredentialsProvider {

    @Override
    public Credentials get() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Authentication");
        System.out.print("Username : ");
        String username = scanner.next();
        System.out.print("Password : ");
        String password = scanner.next();
        return new Credentials(username, password);
    }
}
