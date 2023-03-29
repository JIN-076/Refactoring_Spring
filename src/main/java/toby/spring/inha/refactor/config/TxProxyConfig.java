package toby.spring.inha.refactor.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import toby.spring.inha.refactor.factoryBean.TxProxyFactoryBean;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.service.UserLevelUpgradePolicy;
import toby.spring.inha.refactor.user.service.UserService;
import toby.spring.inha.refactor.user.service.UserServiceImpl;

@Configuration
public class TxProxyConfig {

    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public TxProxyConfig(@Qualifier("userServiceImpl") UserService userService, PlatformTransactionManager transactionManager) {
        this.userService = userService;
        this.transactionManager = transactionManager;
    }

    @Bean
    public TxProxyFactoryBean userService() throws Exception {
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setTarget(this.userService);
        txProxyFactoryBean.setTransactionManager(this.transactionManager);
        txProxyFactoryBean.setPattern("upgradeLevels");
        txProxyFactoryBean.setServiceInterface(UserService.class);
        return txProxyFactoryBean;
    }
}
