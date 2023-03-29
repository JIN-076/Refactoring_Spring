package toby.spring.inha.refactor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.spring.inha.refactor.factoryBean.Message;
import toby.spring.inha.refactor.factoryBean.MessageFactoryBean;

/**
 * beanDefinition = messageConfig, object = toby.spring.inha.refactor.config.MessageConfig$$EnhancerBySpringCGLIB$$418b2426@37d00a23
 * beanDefinition = messageBeans, object = toby.spring.inha.refactor.factoryBean.Message@74d3b638
 * beanDefinition = message, object = toby.spring.inha.refactor.factoryBean.Message@41d7b27f
 */

/**
 * beanDefinition = messageConfig, object = toby.spring.inha.refactor.config.MessageConfig$$EnhancerBySpringCGLIB$$ab12ae4c@1623bbe5
 * beanDefinition = messageFactoryBean, object = toby.spring.inha.refactor.factoryBean.Message@74d3b638
 * beanDefinition = message, object = toby.spring.inha.refactor.factoryBean.Message@41d7b27f
 */

@Configuration
public class MessageConfig {

    @Bean(name = "message")
    public Object messageFactory() throws Exception {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean.getObject();
    }
}
