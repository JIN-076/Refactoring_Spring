package toby.spring.inha.refactor.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import toby.spring.inha.refactor.proxyfactorybean.TransactionAdvice;
import toby.spring.inha.refactor.user.service.UserService;

import java.lang.reflect.Method;

@Configuration
public class TxAdvisorConfig {

    private final TransactionAdvice advice;
    private final UserService userService;

    @Autowired
    public TxAdvisorConfig(TransactionAdvice advice, @Qualifier("userServiceImpl") UserService userService) {
        this.advice = advice;
        this.userService = userService;
    }

    @Bean
    public NameMatchMethodPointcut pointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor advisor() {
        return new DefaultPointcutAdvisor(pointcut(), this.advice);
    }

    @Bean
    public ProxyFactoryBean proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(this.userService);
        proxyFactoryBean.setInterceptorNames("advisor");
        return proxyFactoryBean;
    }

    @Bean
    public UserService userService() {
        return (UserService) proxyFactoryBean().getObject();
    }
}
