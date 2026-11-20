package ma.enset.tpaop.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test d'intégration de l'application complète SpringAopApplication.
 */
class SpringAopApplicationTest {

    @Test
    @DisplayName("REQ-SP-06 : Exécution complète des 4 scénarios de la démonstration Spring AOP")
    void testApplicationScenarios() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            SpringAopApplication.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("DÉMONSTRATION SPRING AOP"), "Le bandeau principal doit s'afficher");
        assertTrue(output.contains("[SCÉNARIO 1]"), "Le scénario 1 doit s'exécuter");
        assertTrue(output.contains("Business Processing ...."), "Le traitement métier process doit s'exécuter");
        assertTrue(output.contains("Business Computing and returning result ...."), "Le calcul compute doit s'exécuter");
        assertTrue(output.contains("Résultat obtenu : 80.0"), "Le résultat 80.0 doit être renvoyé");

        assertTrue(output.contains("[SCÉNARIO 2]"), "Le scénario 2 doit s'exécuter");
        assertTrue(output.contains("Blocage de sécurité attendu : Not Authorized"), "Le blocage doit être effectif");

        assertTrue(output.contains("[SCÉNARIO 3]"), "Le scénario 3 doit s'exécuter");
        assertTrue(output.contains("Rejet d'authentification attendu : Invalid Credentials.."), "Le rejet doit être effectif");

        assertTrue(output.contains("[SCÉNARIO 4]"), "Le scénario 4 doit s'exécuter");
        assertTrue(output.contains("Dans internalCallDemo, l'appel interne à process() n'est pas passé par le proxy"),
                "La limitation du proxy doit être démontrée");
    }
}
