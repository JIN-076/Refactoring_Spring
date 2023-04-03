package toby.spring.inha.refactor.learningTst.pointcut;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import toby.spring.inha.refactor.jdk.target.Bean;
import toby.spring.inha.refactor.jdk.target.Target;

import static org.assertj.core.api.Assertions.*;

public class AspectJTest {

    @Test
    @DisplayName("AspectJ 포인트컷 표현식 테스트")
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(" +
                "public int toby.spring.inha.refactor.jdk.target.Target.minus(int,int) " +
                "throws java.lang.RuntimeException)");

        // Target.minus()
        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("minus", int.class, int.class), null
                )).isTrue();

        // Target.plus()
        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("plus", int.class, int.class), null
                )).isFalse();
    }

    /**
     * 포인트컷과 메서드를 비교해주는 테스트 헬퍼 메서드
     */

    public void pointcutMatches(
            String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz) &&
                pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null
                )).isEqualTo(expected);

    }

    /**
     * 타깃 클래스의 메서드 6개에 대해 포인트컷 선정여부를 검사하는 헬퍼 메서드
     */

    public void targetClassPointcutMatches(String expression, Boolean... expected) throws Exception {

        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    @Test
    @DisplayName("19가지 포인트컷 표현식에 대한 테스트")
    public void pointcut() throws Exception {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* hello(..))", true, true, false, false, false, false);
        targetClassPointcutMatches("execution(* hello())", true, false, false, false, false, false);
        targetClassPointcutMatches("execution(* hello(String))", false, true, false, false, false, false);
        targetClassPointcutMatches("execution(* meth*(..))", false, false, false, false, true, true);
        targetClassPointcutMatches("execution(* *(int, int))", false, false, true, true, false, false);
        targetClassPointcutMatches("execution(* *())", true, false, false, false, true, true);
        targetClassPointcutMatches("execution(* toby.spring.inha.refactor.jdk.target.Target.*(..))",
                true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* toby.spring.inha.refactor.jdk.target.*.*(..))",
                true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* toby.spring.inha.refactor.jdk.target..*.*(..))",
                true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* toby..*.*(..))", true, true, true, true, true, true);
        targetClassPointcutMatches("execution(* com..*.*(..))", false, false, false, false, false, false);
        targetClassPointcutMatches("execution(* *..Target.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..Tar*.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..*get.*(..))", true, true, true, true, true, false);
        targetClassPointcutMatches("execution(* *..B*.*(..))", false, false, false, false, false, true);
        targetClassPointcutMatches("execution(* *..TargetInterface.*(..))", true, true, true, true, false, false);
        targetClassPointcutMatches("execution(* *(..) throws Runtime*)", false, false, false, true, false, true);
        targetClassPointcutMatches("execution(int *(..))", false, false, true, true, false, false);
        targetClassPointcutMatches("execution(void *(..))", true, true, false, false, true, true);
    }
}
