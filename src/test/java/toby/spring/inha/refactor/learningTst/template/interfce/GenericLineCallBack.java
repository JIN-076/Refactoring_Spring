package toby.spring.inha.refactor.learningTst.template.interfce;

public interface GenericLineCallBack<T> {

    T doSomethingWithLine(String line, T value);
}
