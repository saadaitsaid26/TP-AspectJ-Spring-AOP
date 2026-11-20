package ma.enset.tpaop.aspectj.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Journalisation des appels métier et de la création des comptes.
 *
 * <p>La durée est mesurée avec une pile de temps de départ par thread afin
 * que les appels imbriqués (par exemple {@code getCompte} déclenché par
 * {@code PatchAspect}) ne faussent pas la mesure. Le support de cours
 * utilisait deux champs {@code t1}/{@code t2}, corrects uniquement en
 * exécution strictement séquentielle.</p>
 */
@Aspect
public class LogAspect {

    static final Logger LOGGER = Logger.getLogger(LogAspect.class.getName());
    private static final String LOG_FILE = "target/logs/aspectj-demo.log";

    private final ThreadLocal<Deque<Long>> startTimes =
            ThreadLocal.withInitial(ArrayDeque::new);

    public LogAspect() throws IOException {
        Path logPath = Path.of(LOG_FILE).toAbsolutePath();
        Files.createDirectories(logPath.getParent());

        FileHandler fileHandler = new FileHandler(logPath.toString(), true);
        fileHandler.setLevel(Level.INFO);
        LOGGER.addHandler(fileHandler);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        LOGGER.addHandler(consoleHandler);

        LOGGER.setLevel(Level.INFO);
        LOGGER.setUseParentHandlers(false);
    }

    @Pointcut("execution(* ma.enset.tpaop.aspectj.metier.IMetier+.*(..))"
            + " || initialization(ma.enset.tpaop.aspectj.metier.*.new(..))")
    public void businessLayer() {
    }

    @Before("businessLayer()")
    public void before(JoinPoint joinPoint) {
        startTimes.get().push(System.currentTimeMillis());
        LOGGER.info("----------------------------");
        LOGGER.info("Before " + joinPoint.getSignature().toShortString());
    }

    @After("businessLayer()")
    public void after(JoinPoint joinPoint) {
        Deque<Long> starts = startTimes.get();
        Long start = starts.poll();
        long duration = start == null ? -1 : System.currentTimeMillis() - start;
        if (starts.isEmpty()) {
            startTimes.remove();
        }
        LOGGER.info("After " + joinPoint.getSignature().toShortString());
        LOGGER.info("Duration: " + duration + " ms");
        LOGGER.info("****************************");
    }
}
