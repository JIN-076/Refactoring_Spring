package toby.spring.inha.refactor.config;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import toby.spring.inha.refactor.proxyfactorybean.advice.TransactionAdvice;
import toby.spring.inha.refactor.proxyfactorybean.pointcut.NameMatchClassMethodPointcut;
import toby.spring.inha.refactor.user.service.UserService;

@Configuration
public class TxAdvisorConfig {

    private final TransactionAdvice advice;
    private final UserService userService;
    private final TransactionInterceptor txConfigAdvice;

    @Autowired
    public TxAdvisorConfig(
            TransactionAdvice advice,
            @Qualifier("userServiceImpl") UserService userService,
            TransactionInterceptor txConfigAdvice) {
        this.advice = advice;
        this.userService = userService;
        this.txConfigAdvice = txConfigAdvice;
    }

    @Bean
    public NameMatchMethodPointcut simplePointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public NameMatchClassMethodPointcut pointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public AspectJExpressionPointcut aspectJPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* *..*ServiceImpl.upgrade*(..))");
        return pointcut;
    }

    @Bean
    public AspectJExpressionPointcut beanPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor advisor() {
        return new DefaultPointcutAdvisor(beanPointcut(), this.txConfigAdvice);
    }

    @Bean
    public ProxyFactoryBean proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(this.userService);
        proxyFactoryBean.setInterceptorNames("advisor");
        return proxyFactoryBean;
    }

    @Bean(name = "userServiceProxy")
    public UserService userService() {
        return (UserService) proxyFactoryBean().getObject();
    }
}
