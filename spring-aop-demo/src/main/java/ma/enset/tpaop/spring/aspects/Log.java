package ma.enset.tpaop.spring.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation personnalisée servant de pointcut marqueur pour la journalisation
 * et la mesure de performance (cours diapositive 73 et vidéo 5).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
}
