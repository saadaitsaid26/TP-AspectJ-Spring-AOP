package ma.enset.tpaop.aspectj.ui;

import ma.enset.tpaop.aspectj.metier.Compte;
import ma.enset.tpaop.aspectj.metier.IMetier;
import ma.enset.tpaop.aspectj.metier.MetierImpl;

import java.util.Scanner;

/**
 * Application console bancaire reproduisant fidèlement la démonstration
 * pédagogique du cours (diapositives 43-45 et vidéo 4).
 *
 * <p>La méthode {@link #start()} est interceptée par {@code SecurityAspect}
 * via un pointcut {@code call(* ma.enset.tpaop.aspectj.ui.BankApplication.start(..))}.</p>
 */
public class BankApplication {

    private final IMetier metier;
    private final Scanner scanner;
    private final boolean interactive;

    public BankApplication() {
        this(new MetierImpl(), new Scanner(System.in), true);
    }

    public BankApplication(IMetier metier, Scanner scanner, boolean interactive) {
        this.metier = metier;
        this.scanner = scanner;
        this.interactive = interactive;
    }

    public static void main(String[] args) {
        boolean demoMode = args.length > 0 && "--demo".equalsIgnoreCase(args[0]);
        if (demoMode) {
            BankApplication app = new BankApplication(new MetierImpl(), new Scanner(""), false);
            app.start();
        } else {
            new BankApplication().start();
        }
    }

    /**
     * Point d'entrée applicatif. Intercepté par SecurityAspect.
     */
    public void start() {
        System.out.println("Starting Application .....");

        if (!interactive) {
            runAutomatedScenario();
            return;
        }

        try {
            if (!scanner.hasNext()) {
                System.out.println("Aucune entrée interactive détectée. Exécution du scénario de démonstration :");
                runAutomatedScenario();
                return;
            }

            System.out.print("Code : ");
            Long code = scanner.nextLong();
            System.out.print("Solde : ");
            double solde = scanner.nextDouble();
            metier.addCompte(new Compte(code, solde));

            while (true) {
                try {
                    System.out.println("=========================================");
                    System.out.println(metier.getCompte(code));
                    System.out.print("Type Opération (v: verser, r: retirer, q: quitter) : ");
                    String type = scanner.next();
                    if ("q".equalsIgnoreCase(type)) {
                        break;
                    }
                    System.out.print("Montant : ");
                    double montant = scanner.nextDouble();
                    if ("v".equalsIgnoreCase(type)) {
                        metier.verser(code, montant);
                    } else if ("r".equalsIgnoreCase(type)) {
                        metier.retirer(code, montant);
                    } else {
                        System.out.println("Type d'opération inconnu.");
                    }
                } catch (Exception e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Fin de session : " + e.getMessage());
        }
    }

    /**
     * Scénario reproductible pour les tests et exécutions scriptées.
     */
    public void runAutomatedScenario() {
        Long code = 1L;
        double soldeInitial = 5000.0;
        metier.addCompte(new Compte(code, soldeInitial));
        System.out.println("[DEMO] Compte créé : " + metier.getCompte(code));

        System.out.println("[DEMO] Opération 1 : Versement de 3000.0");
        metier.verser(code, 3000.0);
        System.out.println("[DEMO] Solde après versement : " + metier.getCompte(code).getSolde());

        System.out.println("[DEMO] Opération 2 : Retrait valide de 2000.0");
        metier.retirer(code, 2000.0);
        System.out.println("[DEMO] Solde après retrait : " + metier.getCompte(code).getSolde());

        System.out.println("[DEMO] Opération 3 : Retrait invalide de 10000.0 (dépassement de solde)");
        try {
            metier.retirer(code, 10000.0);
        } catch (RuntimeException e) {
            System.out.println("[DEMO] Exception interceptée avec succès : " + e.getMessage());
        }
        System.out.println("[DEMO] Solde final vérifié : " + metier.getCompte(code).getSolde());
    }

    public IMetier getMetier() {
        return metier;
    }
}
