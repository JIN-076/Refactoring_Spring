package toby.spring.inha.refactor.factoryBean;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Setter
@Component(value = "message")
public class MessageFactoryBean implements FactoryBean<Message> {

    public String text;

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
