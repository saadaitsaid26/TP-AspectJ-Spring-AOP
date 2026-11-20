package demo.aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Même comportement que {@link FirstAspect}, mais avec une classe Java
 * annotée. Les deux aspects sont tissés à la compilation par AspectJ.
 */
@Aspect
public class SecondAspect {

    @Pointcut("execution(* *.*.main(..))")
    public void pc1() {
    }

    @Before("pc1()")
    public void beforeMain() {
        System.out.println("Before starting Main @Aspect Syntax ...");
    }

    @After("pc1()")
    public void afterMain() {
        System.out.println("After starting Main @Aspect Syntax ...");
    }
}
