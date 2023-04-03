package toby.spring.inha.refactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collection;
import java.util.Properties;

@Configuration
public class TxPropertiesConfig {

    private PlatformTransactionManager transactionManager;

    @Autowired
    public TxPropertiesConfig(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Properties setTransactionConfig() {
        Properties transactionAttrbitues = new Properties();
        transactionAttrbitues.put("get*", "PROPAGATION_REQUIRED, readOnly, timeout_30");
        transactionAttrbitues.put("upgrade*", "PROPAGATION_REQUIRES_NEW, ISOLATION_SERIALIZABLE");
        transactionAttrbitues.put("*", "PROPAGATION_REQUIRED");
        return transactionAttrbitues;
    }

    @Bean
    public TransactionInterceptor transactionConfig() {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(this.transactionManager);
        interceptor.setTransactionAttributes(setTransactionConfig());
        return interceptor;
    }

    @Bean(name = "txConfigAdvice")
    public TransactionInterceptor txPropertiesConfig() {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED, readOnly");
        transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
        interceptor.setTransactionAttributes(transactionAttributes);
        return interceptor;
    }
}
