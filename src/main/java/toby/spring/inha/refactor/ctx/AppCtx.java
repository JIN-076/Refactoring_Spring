package toby.spring.inha.refactor.ctx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.dao.context.JdbcContext;

import javax.sql.DataSource;

/**
 * beanDefinition = appCtx, object = toby.spring.inha.refactor.ctx.AppCtx$$EnhancerBySpringCGLIB$$5545e069@56781d96
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config
 * beanDefinition = jdbcContext, object = toby.spring.inha.refactor.user.dao.context.JdbcContext@3703bf3c
 * beanDefinition = userDao, object = toby.spring.inha.refactor.user.dao.UserDao@264c5d07
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@baf1bb3
 */

@Configuration
public class AppCtx {

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Bean
    public JdbcContext jdbcContext() {
        JdbcContext jdbcContext = new JdbcContext();
        jdbcContext.setDataSource(dataSourceConfig.mysqlDataSource());
        return jdbcContext;
    }

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao(dataSourceConfig.mysqlDataSource());
        userDao.setJdbcContext(jdbcContext());
        return userDao;
    }
}
