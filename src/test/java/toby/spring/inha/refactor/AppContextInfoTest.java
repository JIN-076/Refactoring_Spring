package toby.spring.inha.refactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.user.dao.UserDaoJdbc;
import toby.spring.inha.refactor.user.dao.mapper.UserMapper;

public class AppContextInfoTest {

    //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppCtx.class, DataSourceConfig.class);
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(UserDaoJdbc.class, DataSourceConfig.class, UserMapper.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findALlBean() {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinition : beanDefinitionNames) {
            Object bean = context.getBean(beanDefinition);
            System.out.println("beanDefinition = " + beanDefinition + ", object = " + bean);
        }
    }
}
