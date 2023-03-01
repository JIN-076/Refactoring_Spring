package toby.spring.inha.refactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.ctx.AppCtx;

/**
 * beanDefinition = appCtx, object = toby.spring.inha.refactor.ctx.AppCtx$$EnhancerBySpringCGLIB$$6ca8c71e@41477a6d
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config
 * beanDefinition = userDao, object = toby.spring.inha.refactor.user.dao.UserDao@1eea9d2d
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@1b9c1b51
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppCtx.class, DataSourceConfig.class})
//@EnableConfigurationProperties(value = {AppCtx.class, DataSourceConfig.class})
@TestPropertySource("classpath:/application.properties")
public class AnnotationAppContextTest {

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("Annotation loading DI Container for finding Bean")
    public void findAllBean() {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinition : beanDefinitionNames) {
            Object bean = context.getBean(beanDefinition);
            System.out.println("beanDefinition = " + beanDefinition + ", object = " + bean);
        }
    }

}
