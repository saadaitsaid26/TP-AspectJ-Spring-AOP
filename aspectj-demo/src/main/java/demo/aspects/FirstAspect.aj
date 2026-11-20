package demo.aspects;

/**
 * Aspect écrit avec la syntaxe native AspectJ (.aj), comme dans le support
 * de cours (page 15).
 */
public aspect FirstAspect {

    /*
     * La précédence rend l'ordre des conseils déterministe et reproduit la
     * sortie attendue du cours : FirstAspect avant, SecondAspect après.
     */
    declare precedence: FirstAspect, SecondAspect;

    pointcut test(): execution(* *.*.main(..));

    before(): test() {
        System.out.println("Before starting Main AspectJ Syntax...");
    }

    after(): test() {
        System.out.println("After Finishing Main AspectJ Syntax...");
    }
}
