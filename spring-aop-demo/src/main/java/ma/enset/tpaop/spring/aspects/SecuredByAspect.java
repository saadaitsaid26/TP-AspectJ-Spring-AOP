package ma.enset.tpaop.spring.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation personnalisée de sécurité basée sur les rôles (RBAC),
 * conforme aux diapositives 76-78 du support de cours et à la vidéo 5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredByAspect {
    String[] roles();
}
