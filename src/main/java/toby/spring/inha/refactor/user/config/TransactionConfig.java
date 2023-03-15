package toby.spring.inha.refactor.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Component
public class TransactionConfig {

    private final DataSource dataSource;

    @Autowired
    public TransactionConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManagerJDBC() {
        return new DataSourceTransactionManager(this.dataSource);
    }

    /**
    @Bean
    public PlatformTransactionManager transactionManagerJPA() {
        return new JpaTransactionManager();
    }

    @Bean
    public PlatformTransactionManager transactionManagerJTA() {
        return new JtaTransactionManager();
    }

    @Bean
    public PlatformTransactionManager transactionManagerHibernate() {
        return new HibernateTransactionManager();
    }
    */
}
