package ma.enset.tpaop.spring.metier;

/**
 * Contrat de service métier Spring AOP (diapositives 66-67 et vidéo 5).
 */
public interface IMetier {

    /**
     * Traitement métier accessible aux rôles ADMIN et USER, journalisé par @Log.
     */
    void process();

    /**
     * Calcul métier restreint au rôle ADMIN uniquement.
     */
    double compute();

    /**
     * Méthode démontrant la limitation de self-invocation des proxys Spring AOP.
     */
    void internalCallDemo();
}
