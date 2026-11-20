package ma.enset.tpaop.aspectj.aspects;

import ma.enset.tpaop.aspectj.metier.Compte;
import ma.enset.tpaop.aspectj.metier.MetierImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Aspect correctif (patch) qui refuse un retrait supérieur au solde.
 * Le code métier reste inchangé : la règle est greffée autour de
 * l'exécution de {@link MetierImpl#retirer(Long, double)}.
 */
@Aspect
public class PatchAspect {

    @Around("execution(* ma.enset.tpaop.aspectj.metier.MetierImpl.retirer(..)) && args(code, mt)")
    public Object patch(ProceedingJoinPoint proceedingJoinPoint, Long code, double mt) throws Throwable {
        MetierImpl metier = (MetierImpl) proceedingJoinPoint.getTarget();
        Compte compte = metier.getCompte(code);
        if (compte == null) {
            throw new IllegalArgumentException("Compte inexistant : " + code);
        }

        System.out.println("PatchAspect: vérification du solde avant retrait de " + mt
                + " (solde = " + compte.getSolde() + ")");

        if (compte.getSolde() >= mt) {
            return proceedingJoinPoint.proceed();
        }
        throw new RuntimeException("Solde insuffisant");
    }
}
