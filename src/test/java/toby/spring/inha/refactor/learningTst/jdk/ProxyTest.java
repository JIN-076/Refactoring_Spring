package toby.spring.inha.refactor.learningTst.jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toby.spring.inha.refactor.jdk.Hello;
import toby.spring.inha.refactor.jdk.proxy.HelloUppercase;
import toby.spring.inha.refactor.jdk.proxy.UppercaseHandler;
import toby.spring.inha.refactor.jdk.proxy.UppercaseHandlerV2;
import toby.spring.inha.refactor.jdk.target.HelloTarget;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTest {

    @Test
    @DisplayName("프록시, 데코레이터 패턴 테스트")
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    @DisplayName("다이내믹 프록시 테스트")
    public void dynamicProxy() {
        // InvocationHandler를 사용하고 Hello 인터페이스를 구현하는 프록시 생성
        // 다이내믹 프록시 생성은 Proxy 클래스의 newProxyInstance 스태틱 메서드 사용
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");

    }

    @Test
    @DisplayName("다이내믹 프록시 확장 테스트")
    public void dynamicProxyExtension() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ Hello.class},
                new UppercaseHandlerV2(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }
}
