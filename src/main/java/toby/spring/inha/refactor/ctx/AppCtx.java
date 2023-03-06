package toby.spring.inha.refactor.ctx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.dao.context.JdbcContext;
import toby.spring.inha.refactor.user.dao.mapper.UserMapper;
import toby.spring.inha.refactor.user.domain.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * beanDefinition = appCtx, object = toby.spring.inha.refactor.ctx.AppCtx$$EnhancerBySpringCGLIB$$5545e069@56781d96
 * beanDefinition = dataSourceConfig, object = toby.spring.inha.refactor.config
 * beanDefinition = jdbcContext, object = toby.spring.inha.refactor.user.dao.context.JdbcContext@3703bf3c
 * beanDefinition = userDao, object = toby.spring.inha.refactor.user.dao.UserDaoV1@264c5d07
 * beanDefinition = mysqlDataSource, object = org.springframework.jdbc.datasource.DriverManagerDataSource@baf1bb3
 */

//@Configuration
public class AppCtx {

    /**
    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    private RowMapper<User> userRowMapper;
    */

    /**
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSourceConfig.mysqlDataSource());
        userDao.setUserRowMapper(userRowMapper);
        return userDao;
    }
    */
}
