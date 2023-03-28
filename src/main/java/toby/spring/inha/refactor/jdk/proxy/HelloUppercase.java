package toby.spring.inha.refactor.jdk.proxy;

import toby.spring.inha.refactor.jdk.Hello;

public class HelloUppercase implements Hello {

    Hello hello;

    public HelloUppercase(Hello hello) {
        this.hello = hello;
    }

    @Override
    public String sayHello(String name) {
        return hello.sayHello("Toby").toUpperCase();
    }

    @Override
    public String sayHi(String name) {
        return hello.sayHi("Toby").toUpperCase();
    }

    @Override
    public String sayThankYou(String name) {
        return hello.sayThankYou("Toby").toUpperCase();
    }
}
