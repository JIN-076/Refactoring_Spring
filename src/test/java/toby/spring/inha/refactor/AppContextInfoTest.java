package toby.spring.inha.refactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.config.TxAdvisorConfig;
import toby.spring.inha.refactor.config.TxProxyConfig;
import toby.spring.inha.refactor.factoryBean.TxProxyFactoryBean;
import toby.spring.inha.refactor.proxyfactorybean.TransactionAdvice;
import toby.spring.inha.refactor.user.config.MailSenderConfig;
import toby.spring.inha.refactor.user.config.TransactionConfig;
import toby.spring.inha.refactor.user.dao.UserDaoJdbc;
import toby.spring.inha.refactor.user.dao.mapper.UserMapper;
import toby.spring.inha.refactor.user.service.EmailPolicy;
import toby.spring.inha.refactor.user.service.UserLevelUpgradePolicyImpl;
import toby.spring.inha.refactor.user.service.UserServiceImpl;
import toby.spring.inha.refactor.user.service.UserServiceTx;

/**
 * beanDefinition = txProxyConfig, object = toby.spring.inha.refactor.config.TxProxyConfig$$EnhancerBySpringCGLIB$$1b7ebf00@367795c7
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config.DataSourceConfig$$EnhancerBySpringCGLIB$$cf5fbbf1@6b739528
 * beanDefinition = mailSenderConfig, object = toby.spring.inha.refactor.user.config.MailSenderConfig$$EnhancerBySpringCGLIB$$839396d7@1e7f2e0f
 * beanDefinition = transactionConfig, object = toby.spring.inha.refactor.user.config.TransactionConfig@49298ce7
 *
 * beanDefinition = userMapper, object = toby.spring.inha.refactor.user.dao.mapper.UserMapper$$EnhancerBySpringCGLIB$$970d11ba@3b4ef7
 * beanDefinition = userDaoJdbc, object = toby.spring.inha.refactor.user.dao.UserDaoJdbc@61019f59
 * beanDefinition = emailPolicy, object = toby.spring.inha.refactor.user.service.EmailPolicy@7f34a967
 * beanDefinition = userLevelUpgradePolicyImpl, object = toby.spring.inha.refactor.user.service.UserLevelUpgradePolicyImpl@77e80a5e
 *
 * beanDefinition = userServiceImpl, object = toby.spring.inha.refactor.user.service.UserServiceImpl@1d8e2eea
 * beanDefinition = userServiceTx, object = toby.spring.inha.refactor.user.service.UserServiceTx@680362a
 * beanDefinition = userService, object = toby.spring.inha.refactor.user.service.UserServiceImpl@1d8e2eea
 *
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@aa5455e
 * beanDefinition = mailSender, object = org.springframework.mail.javamail.JavaMailSenderImpl@7fcff1b9
 * beanDefinition = dummyMailSender, object = toby.spring.inha.refactor.user.service.DummyMailSender@4aa3d36
 * beanDefinition = transactionManagerJDBC, object = org.springframework.jdbc.datasource.DataSourceTransactionManager@5a4ed68f
 * beanDefinition = userRowMapper, object = toby.spring.inha.refactor.user.dao.mapper.UserMapper$1@4a8b5227
 */

public class AppContextInfoTest {

    // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppCtx.class, DataSourceConfig.class);
    // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(UserDaoJdbc.class, DataSourceConfig.class, UserMapper.class);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TransactionAdvice.class,
            TxAdvisorConfig.class, DataSourceConfig.class, MailSenderConfig.class, TransactionConfig.class,
            UserMapper.class, EmailPolicy.class, UserLevelUpgradePolicyImpl.class,
            UserDaoJdbc.class, UserServiceImpl.class, UserServiceTx.class);

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
