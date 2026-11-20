package ma.enset.tpaop.spring.metier;

import ma.enset.tpaop.spring.aspects.Log;
import ma.enset.tpaop.spring.aspects.SecuredByAspect;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service métier Spring.
 * Les préoccupations techniques (sécurité et journalisation) sont exprimées
 * de manière déclarative par des annotations d'aspect.
 */
@Service
public class MetierImpl implements IMetier {

    @Override
    @Log
    @SecuredByAspect(roles = {"ADMIN", "USER"})
    public void process() {
        System.out.println("Business Processing ....");
    }

    @Override
    @SecuredByAspect(roles = {"ADMIN"})
    public double compute() {
        double data = 80.0;
        System.out.println("Business Computing and returning result ....");
        return data;
    }

    @Override
    public void internalCallDemo() {
        System.out.println("[INTERNE] Appel interne direct de this.process() sans passer par le proxy Spring...");
        this.process();
    }
}
