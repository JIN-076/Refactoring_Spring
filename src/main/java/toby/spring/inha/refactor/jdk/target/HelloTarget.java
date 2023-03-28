package toby.spring.inha.refactor.jdk.target;

import toby.spring.inha.refactor.jdk.Hello;

public class HelloTarget implements Hello {

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public String sayHi(String name) {
        return "Hi " + name;
    }

    public String sayThankYou(String name) {
        return "Thank You " + name;
    }
}
