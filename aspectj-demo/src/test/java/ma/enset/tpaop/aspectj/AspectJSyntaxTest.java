package ma.enset.tpaop.aspectj;

import demo.MainAdviceDemo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validation de la syntaxe native AspectJ (.aj) et de la syntaxe annotée (@Aspect),
 * ainsi que de l'ordre de préséance déterminé par 'declare precedence'.
 */
class AspectJSyntaxTest {

    @Test
    @DisplayName("REQ-AJ-01 / REQ-AJ-02 / REQ-AJ-03 : Tissage statique et préséance FirstAspect (.aj) / SecondAspect (@Aspect)")
    void testNativeAndAnnotatedSyntaxOrder() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            MainAdviceDemo.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);

        // Vérification du déclenchement des aspects sur main(..)
        assertTrue(output.contains("Before starting Main AspectJ Syntax..."),
                "Le conseil Before de FirstAspect (.aj) doit être exécuté");
        assertTrue(output.contains("Before starting Main @Aspect Syntax ..."),
                "Le conseil Before de SecondAspect (@Aspect) doit être exécuté");
        assertTrue(output.contains("Application starting ...."),
                "Le corps de la méthode main doit être exécuté");
        assertTrue(output.contains("After starting Main @Aspect Syntax ..."),
                "Le conseil After de SecondAspect doit être exécuté");
        assertTrue(output.contains("After Finishing Main AspectJ Syntax..."),
                "Le conseil After de FirstAspect doit être exécuté");

        // Vérification de l'ordre strict déterminé par declare precedence: FirstAspect, SecondAspect;
        int idxBefore1 = output.indexOf("Before starting Main AspectJ Syntax...");
        int idxBefore2 = output.indexOf("Before starting Main @Aspect Syntax ...");
        int idxMain = output.indexOf("Application starting ....");
        int idxAfter2 = output.indexOf("After starting Main @Aspect Syntax ...");
        int idxAfter1 = output.indexOf("After Finishing Main AspectJ Syntax...");

        assertTrue(idxBefore1 < idxBefore2, "FirstAspect.before doit précéder SecondAspect.before");
        assertTrue(idxBefore2 < idxMain, "SecondAspect.before doit précéder main");
        assertTrue(idxMain < idxAfter2, "main doit précéder SecondAspect.after");
        assertTrue(idxAfter2 < idxAfter1, "SecondAspect.after doit précéder FirstAspect.after");
    }
}
