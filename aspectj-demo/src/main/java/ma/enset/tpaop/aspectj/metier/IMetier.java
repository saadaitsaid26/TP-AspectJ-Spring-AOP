package ma.enset.tpaop.aspectj.metier;

/**
 * Contrat métier de gestion des comptes bancaires.
 */
public interface IMetier {

    void addCompte(Compte cp);

    void verser(Long code, double mt);

    void retirer(Long code, double mt);

    Compte getCompte(Long code);
}
