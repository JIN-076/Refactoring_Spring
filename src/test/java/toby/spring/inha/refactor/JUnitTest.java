package toby.spring.inha.refactor;


import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestPropertySource("classpath:/application-dev.properties")
public class JUnitTest {

    private static JUnitTest testObject;
    private static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();

    @Autowired
    private ApplicationContext context;

    private static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        Assertions.assertThat(this).isNotSameAs(testObject);
        System.out.println("test1 : this -> " + this + " testObject -> " + testObject);
        testObject = this;
        System.out.println("after test1 : " + this);
    }

    @Test
    public void test2() {
        Assertions.assertThat(this).isNotSameAs(testObject);
        System.out.println("test2 : this -> " + this + " testObject -> " + testObject);
        testObject = this;
        System.out.println("after test2 : " + this);
    }

    @Test
    public void test3() {
        Assertions.assertThat(this).isNotSameAs(testObject);
        System.out.println("test3 : this -> " + this + " testObject -> " + testObject);
        testObject = this;
        System.out.println("after test3 : " + this);
    }

    @Test
    public void advancedTest1() {
        Assertions.assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        Assertions.assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    public void advancedTest2() {
        Assertions.assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        Assertions.assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @Test
    public void advancedTest3() {
        Assertions.assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        Assertions.assertThat(contextObject).satisfiesAnyOf(
                contextObject -> Assertions.assertThat(contextObject).isNull(),
                contextObject -> Assertions.assertThat(contextObject).isSameAs(this.context)
        );
    }


}
