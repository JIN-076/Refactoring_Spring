package toby.spring.inha.refactor.learningTst.factoryBean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toby.spring.inha.refactor.config.MessageConfig;
import toby.spring.inha.refactor.factoryBean.Message;
import toby.spring.inha.refactor.factoryBean.MessageFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MessageConfig.class, MessageFactoryBean.class})
public class FactoryBeanTest {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("FactoryBean 빈 가져오기")
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
    }

    @Test
    @DisplayName("FactoryBean 자체 가져오기")
    public void getFactoryBean() throws Exception {
        Object factory = context.getBean("&message");
        assertThat(factory).isInstanceOf(MessageFactoryBean.class);
    }
}
