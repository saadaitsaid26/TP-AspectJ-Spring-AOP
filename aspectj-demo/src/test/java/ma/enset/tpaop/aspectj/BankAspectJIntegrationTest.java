package ma.enset.tpaop.aspectj;

import ma.enset.tpaop.aspectj.aspects.SecurityAspect;
import ma.enset.tpaop.aspectj.metier.Compte;
import ma.enset.tpaop.aspectj.metier.IMetier;
import ma.enset.tpaop.aspectj.metier.MetierImpl;
import ma.enset.tpaop.aspectj.security.FixedCredentialsProvider;
import ma.enset.tpaop.aspectj.ui.BankApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration pour les aspects tissés par AspectJ :
 * PatchAspect (retrait avec contrôle du solde),
 * SecurityAspect (authentification via join point call),
 * LogAspect (journalisation automatique et persistance).
 */
class BankAspectJIntegrationTest {

    private IMetier metier;

    @BeforeEach
    void setUp() {
        metier = new MetierImpl();
    }

    @AfterEach
    void tearDown() {
        SecurityAspect.resetCredentialsProvider();
    }

    @Test
    @DisplayName("REQ-AJ-07 : PatchAspect autorise le retrait si solde suffisant")
    void testPatchAspectAllowsWithdrawal() {
        metier.addCompte(new Compte(1L, 1000.0));
        metier.retirer(1L, 400.0);

        assertEquals(600.0, metier.getCompte(1L).getSolde(), 0.001);
    }

    @Test
    @DisplayName("REQ-AJ-07 : PatchAspect refuse le retrait et lève RuntimeException si solde insuffisant")
    void testPatchAspectRefusesWithdrawalWhenInsufficientBalance() {
        metier.addCompte(new Compte(2L, 500.0));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            metier.retirer(2L, 800.0);
        });

        assertEquals("Solde insuffisant", exception.getMessage());
        // Le solde ne doit pas avoir été débité
        assertEquals(500.0, metier.getCompte(2L).getSolde(), 0.001);
    }

    @Test
    @DisplayName("REQ-AJ-06 : SecurityAspect autorise l'accès avec les bons identifiants (root / 123)")
    void testSecurityAspectAccessGranted() {
        SecurityAspect.setCredentialsProvider(new FixedCredentialsProvider("root", "123"));

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            BankApplication app = new BankApplication(metier, new Scanner(""), false);
            app.start();
        } finally {
            System.setOut(originalOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Authentication"), "L'aspect doit afficher la demande d'authentification");
        assertTrue(output.contains("Before starting"), "L'aspect doit afficher 'Before starting'");
        assertTrue(output.contains("Starting Application ....."), "La méthode start() doit être exécutée");
        assertTrue(output.contains("End of Application"), "L'aspect doit afficher 'End of Application'");
    }

    @Test
    @DisplayName("REQ-AJ-06 : SecurityAspect refuse l'accès avec des identifiants invalides")
    void testSecurityAspectAccessDenied() {
        SecurityAspect.setCredentialsProvider(new FixedCredentialsProvider("hacker", "badpass"));

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            BankApplication app = new BankApplication(metier, new Scanner(""), false);
            app.start();
        } finally {
            System.setOut(originalOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Access denied ..."), "L'aspect doit refuser l'accès");
        assertFalse(output.contains("Starting Application ....."), "La méthode start() NE DOIT PAS être appelée");
    }

    @Test
    @DisplayName("REQ-AJ-05 : LogAspect consigne les appels métier dans le fichier log")
    void testLoggingAspectWeaving() {
        metier.addCompte(new Compte(5L, 3000.0));
        metier.verser(5L, 1000.0);

        File logFile = new File("target/logs/aspectj-demo.log");
        assertTrue(logFile.exists(), "Le fichier de journalisation doit être généré par LogAspect");
        assertTrue(logFile.length() > 0, "Le fichier de log ne doit pas être vide");
    }
}
