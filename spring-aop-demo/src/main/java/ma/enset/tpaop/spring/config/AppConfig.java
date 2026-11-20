package ma.enset.tpaop.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration Spring basée sur les annotations Java (cours p. 69).
 * L'annotation {@link EnableAspectJAutoProxy} active la détection automatique
 * des beans annotés {@code @Aspect} et l'habillage des beans cibles par proxy dynamique.
 */
@Configuration
@ComponentScan(basePackages = "ma.enset.tpaop.spring")
@EnableAspectJAutoProxy
public class AppConfig {
}
