package ma.enset.tpaop.aspectj.metier;

import java.util.HashMap;
import java.util.Map;

/**
 * Implémentation en mémoire du contrat métier. Aucun code technique
 * (journalisation, sécurité, contrôle de retrait) n'y figure : ces
 * préoccupations sont portées par les aspects.
 */
public class MetierImpl implements IMetier {

    private final Map<Long, Compte> comptes = new HashMap<>();

    @Override
    public void addCompte(Compte cp) {
        comptes.put(cp.getCode(), cp);
    }

    @Override
    public void verser(Long code, double mt) {
        Compte cp = comptes.get(code);
        if (cp == null) {
            throw new IllegalArgumentException("Compte inexistant : " + code);
        }
        cp.setSolde(cp.getSolde() + mt);
    }

    @Override
    public void retirer(Long code, double mt) {
        Compte cp = comptes.get(code);
        if (cp == null) {
            throw new IllegalArgumentException("Compte inexistant : " + code);
        }
        cp.setSolde(cp.getSolde() - mt);
    }

    @Override
    public Compte getCompte(Long code) {
        return comptes.get(code);
    }
}
