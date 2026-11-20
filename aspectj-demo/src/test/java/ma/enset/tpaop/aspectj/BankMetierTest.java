package ma.enset.tpaop.aspectj;

import ma.enset.tpaop.aspectj.metier.Compte;
import ma.enset.tpaop.aspectj.metier.IMetier;
import ma.enset.tpaop.aspectj.metier.MetierImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires du modèle métier bancaire (Compte, IMetier, MetierImpl).
 */
class BankMetierTest {

    private IMetier metier;

    @BeforeEach
    void setUp() {
        metier = new MetierImpl();
    }

    @Test
    @DisplayName("REQ-AJ-04 : Ajout et consultation d'un compte bancaire")
    void testAddAndConsultCompte() {
        Compte compte = new Compte(10L, 2500.0);
        metier.addCompte(compte);

        Compte retrieved = metier.getCompte(10L);
        assertNotNull(retrieved);
        assertEquals(10L, retrieved.getCode());
        assertEquals(2500.0, retrieved.getSolde(), 0.001);
    }

    @Test
    @DisplayName("REQ-AJ-04 : Versement bancaire")
    void testVerser() {
        metier.addCompte(new Compte(20L, 1000.0));
        metier.verser(20L, 500.0);

        assertEquals(1500.0, metier.getCompte(20L).getSolde(), 0.001);
    }

    @Test
    @DisplayName("Gestion d'exception sur compte inexistant")
    void testCompteInexistant() {
        assertThrows(IllegalArgumentException.class, () -> metier.verser(999L, 100.0));
        assertThrows(IllegalArgumentException.class, () -> metier.retirer(999L, 100.0));
    }
}
