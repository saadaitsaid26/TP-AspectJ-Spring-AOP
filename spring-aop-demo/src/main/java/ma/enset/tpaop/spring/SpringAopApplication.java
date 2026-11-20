package ma.enset.tpaop.spring;

import ma.enset.tpaop.spring.config.AppConfig;
import ma.enset.tpaop.spring.metier.IMetier;
import ma.enset.tpaop.spring.security.SecurityContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Classe principale de démonstration pour Spring AOP, reproduisant le
 * scénario des diapositives 71-72 du cours et de la vidéo 5.
 */
public class SpringAopApplication {

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println(" DÉMONSTRATION SPRING AOP (PROXYS DYNAMIQUES & ASPECTS) ");
        System.out.println("===============================================================");

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            IMetier metier = context.getBean(IMetier.class);

            System.out.println("\n[SCÉNARIO 1] Authentification ADMIN (root / 1234) :");
            SecurityContext.authenticateUser("root", "1234", new String[]{"ADMIN"});
            System.out.println("-> Appel de process() [autorisé ADMIN/USER, avec @Log] :");
            metier.process();
            System.out.println("-> Appel de compute() [autorisé ADMIN uniquement] :");
            double result = metier.compute();
            System.out.println("Résultat obtenu : " + result);

            System.out.println("\n---------------------------------------------------------------");
            System.out.println("[SCÉNARIO 2] Authentification USER (root / 1234 avec rôle USER) :");
            SecurityContext.authenticateUser("root", "1234", new String[]{"USER"});
            System.out.println("-> Appel de process() [autorisé USER] :");
            metier.process();
            System.out.println("-> Appel de compute() [exige ADMIN] :");
            try {
                metier.compute();
            } catch (RuntimeException e) {
                System.out.println("Blocage de sécurité attendu : " + e.getMessage());
            }

            System.out.println("\n---------------------------------------------------------------");
            System.out.println("[SCÉNARIO 3] Authentification invalide :");
            try {
                SecurityContext.authenticateUser("baduser", "wrongpwd", new String[]{"ADMIN"});
            } catch (RuntimeException e) {
                System.out.println("Rejet d'authentification attendu : " + e.getMessage());
            }

            System.out.println("\n---------------------------------------------------------------");
            System.out.println("[SCÉNARIO 4] Démonstration de la limitation du proxy (Self-Invocation) :");
            SecurityContext.authenticateUser("root", "1234", new String[]{"ADMIN"});
            System.out.println("-> Appel externe via proxy de internalCallDemo() :");
            metier.internalCallDemo();
            System.out.println("Note : Dans internalCallDemo, l'appel interne à process() n'est pas passé par le proxy.");

            System.out.println("\n===============================================================");
            System.out.println(" FIN DE LA DÉMONSTRATION SPRING AOP ");
            System.out.println("===============================================================");
        }
    }
}
