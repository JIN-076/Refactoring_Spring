package toby.spring.inha.refactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.user.dao.UserDaoJdbc;
import toby.spring.inha.refactor.user.dao.mapper.UserMapper;

/**
 * @Configuration 수동 주입 방식과 수정자, 필드 주입 방식을 이용한 Bean Register
 *
 * beanDefinition = appCtx, object = toby.spring.inha.refactor.ctx.AppCtx$$EnhancerBySpringCGLIB$$6ca8c71e@41477a6d
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config
 * beanDefinition = userDao, object = toby.spring.inha.refactor.user.dao.UserDaoV1@1eea9d2d
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@1b9c1b51
 */

/**
 * @Component 자동 주입 방식과 생성자 주입 방식을 이용한 Bean Register
 *
 * beanDefinition = userDao, object = toby.spring.inha.refactor.user.dao.UserDao@67a056f1
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config.DataSourceConfig$$EnhancerBySpringCGLIB$$e675979b@1ec7d8b3
 * beanDefinition = userMapper, object = toby.spring.inha.refactor.user.dao.mapper.UserMapper$$EnhancerBySpringCGLIB$$ae22ed64@bd2f5a9
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@2e52fb3e
 * beanDefinition = userRowMapper, object = toby.spring.inha.refactor.user.dao.mapper.UserMapper$1@72805168
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserDaoJdbc.class, DataSourceConfig.class, UserMapper.class})
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
