package toby.spring.inha.refactor.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import toby.spring.inha.refactor.config.*;
import toby.spring.inha.refactor.factoryBean.TxProxyFactoryBean;
import toby.spring.inha.refactor.jdk.proxy.TransactionHandler;
import toby.spring.inha.refactor.proxyfactorybean.advice.TransactionAdvice;
import toby.spring.inha.refactor.user.config.MailSenderConfig;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.dao.UserDaoJdbc;
import toby.spring.inha.refactor.user.dao.mapper.UserMapper;
import toby.spring.inha.refactor.user.dao.mock.MockUserDao;
import toby.spring.inha.refactor.user.domain.Level;
import toby.spring.inha.refactor.user.domain.User;
import toby.spring.inha.refactor.user.service.*;
import toby.spring.inha.refactor.user.config.TransactionConfig;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ComponentScan(
        basePackages = {"toby.spring.inha.refactor"},
        basePackageClasses = UserServiceImpl.class
)
@ContextConfiguration(classes = {TxPropertiesConfig.class, BeanPostProcessorConfig.class, UserServiceTest.class, TxAdvisorConfig.class, TxProxyConfig.class, TransactionAdvice.class, EmailPolicy.class, MailSenderConfig.class, TransactionConfig.class, UserServiceImpl.class, UserLevelUpgradePolicyImpl.class, UserDaoJdbc.class, DataSourceConfig.class, UserMapper.class})
public class UserServiceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("userServiceProxy")
    private UserService userService;

    @Autowired
    @Qualifier("testUserService")
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EmailPolicy emailPolicy;

    List<User> users;

    static class TestUserPolicyException extends RuntimeException {}

    static class TestUserPolicy extends UserLevelUpgradePolicyImpl {

        private String id;

        private TestUserPolicy(UserDao userDao, EmailPolicy emailPolicy, String id) {
            super(userDao, emailPolicy);
            this.id = id;
        }

        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserPolicyException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserService extends UserServiceImpl {

        public TestUserService(UserDao userDao) {
            super(userDao);
        }
    }

    @Component
    @Primary
    @Qualifier("testPolicy")
    static class TestUserLevelPolicy extends UserLevelUpgradePolicyImpl {

        private String id = "madDitto";

        @Autowired
        private TestUserLevelPolicy(UserDao userDao, EmailPolicy emailPolicy) {
            super(userDao, emailPolicy);
        }

        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserPolicyException();
            super.upgradeLevel(user);
        }
    }

    @Component
    @Qualifier("testUserService")
    static class TestUserServiceImpl extends UserServiceImpl {

        private UserLevelUpgradePolicy testPolicy;

        @Autowired
        public TestUserServiceImpl(UserDao userDao, @Qualifier("testPolicy") UserLevelUpgradePolicy testPolicy) {
            super(userDao);
            this.testPolicy = testPolicy;
            super.setPolicy(this.testPolicy);
        }

        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }
    }

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumJin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "bumJin@gmail.com"),
                new User("joyTouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "joyTouch@gmail.com"),
                new User("erWins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "erWins@gmail.com"),
                new User("madDitto", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "madDitto@gmail.com"),
                new User("madGreen", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "madGreen@gmail.com")
        );
    }

    @Test
    @DisplayName("userService Bean 확인")
    public void bean() {
        assertThat(this.userService).isNotNull();
    }

    @Test
    @DisplayName("upgradeLevels 테스트")
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        //userService.upgradeLevelsRfc3();
        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @Test
    @DisplayName("add 테스트")
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());
    }

    @Test
    @DisplayName("upgrade 중단 테스트")
    public void upgradeAllOrNothing() {

        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao);
        testUserService.setPolicy(userLevelUpgradePolicy);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            // testUserService.upgradeLevelsRfc();
            testUserService.upgradeLevels();

            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) {} // TestUserService 가 던지는 예외를 잡아 계속 진행되도록 한다.

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    @DisplayName("Transaction 테스트")
    public void upgradeAllOrNothingRfc() throws Exception {
        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao);
        testUserService.setPolicy(userLevelUpgradePolicy);
        UserServiceTx testUserServiceTx = new UserServiceTx();

        testUserServiceTx.setUserService(testUserService);
        testUserServiceTx.setDataSource(this.dataSource);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            // testUserService.upgradeLevelsRfc3();
            testUserServiceTx.upgradeLevelsOld();

            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) {} // TestUserService 가 던지는 예외를 잡아 계속 진행되도록 한다.

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    @DisplayName("트랜잭션 추상화 API 테스트")
    public void upgradeAllOrNothingRfc2() throws Exception {
        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao);
        testUserService.setPolicy(userLevelUpgradePolicy);
        UserServiceTx testUserServiceTx = new UserServiceTx();

        testUserServiceTx.setUserService(testUserService);
        testUserServiceTx.setTransactionManager(this.transactionManager);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserServiceTx.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) {} // TestUserService 가 던지는 예외를 잡아 계속 진행되도록 한다.

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    @DirtiesContext
    @DisplayName("Mock Object 이용한 테스트")
    public void upgradeLevelsMock() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        emailPolicy.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DisplayName("MockUserDao를 사용한 고립된 테스트")
    public void upgradeLevelsMockV2() throws Exception {
        MockUserDao mockUserDao = new MockUserDao(this.users);

        MockMailSender mockMailSender = new MockMailSender();
        emailPolicy.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new UserLevelUpgradePolicyImpl(mockUserDao, emailPolicy);
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao);
        userServiceImpl.setPolicy(userLevelUpgradePolicy);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "joyTouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madDitto", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    @Test
    @DisplayName("Mockito를 이용한 MockUserDao 테스트")
    public void mockUpgradeLevels() throws Exception {
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);

        MailSender mockMailSender = mock(MailSender.class);
        emailPolicy.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new UserLevelUpgradePolicyImpl(mockUserDao, emailPolicy);
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao);
        userServiceImpl.setPolicy(userLevelUpgradePolicy);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        // 파라미터를 정밀하게 검사하기 위해 캡처할 수도 있다.
        verify(mockMailSender, times(2)).send(mailMessageArgumentCaptor.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    @Test
    @DisplayName("TransactionHandler와 다이내믹 프록시를 이용한 테스트")
    public void upgradeAllOrNothingProxy() throws Exception {
        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        UserServiceImpl userServiceImpl = new UserServiceImpl(this.userDao);
        userServiceImpl.setPolicy(userLevelUpgradePolicy);

        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTarget(userServiceImpl);
        transactionHandler.setTransactionManager(this.transactionManager);
        transactionHandler.setPattern("upgradeLevels");
        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ UserService.class },
                transactionHandler);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) { }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    @DirtiesContext
    @DisplayName("Transaction Factory Bean 테스트")
    public void upgradeAllOrNothingFactoryBean() throws Exception {
        MailSender mockMailSender = mock(MailSender.class);
        emailPolicy.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        TestUserService testUserService = new TestUserService(this.userDao);
        testUserService.setPolicy(userLevelUpgradePolicy);

        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&txProxyFactory", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) { }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    @DisplayName("ProxyFactoryBean 테스트")
    public void upgradeAllOrNothingAdvisor() {
        MailSender mockMailSender = mock(MailSender.class);
        emailPolicy.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserPolicy(this.userDao, this.emailPolicy, users.get(3).getId());
        TestUserService testUserService = new TestUserService(this.userDao);
        testUserService.setPolicy(userLevelUpgradePolicy);

        ProxyFactoryBean txProxyFactoryBean = context.getBean("&proxyFactoryBean", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) { }

        checkLevelUpgraded(users.get(1), false);
        assertThat(AopUtils.isJdkDynamicProxy(txUserService)).isTrue();
        assertThat(txUserService).isInstanceOf(java.lang.reflect.Proxy.class);
    }

    @Test
    @DisplayName("자동 프록시 생성 테스트")
    public void upgradeAllOrNothingAutoProxy() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            this.testUserService.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) { }

        checkLevelUpgraded(users.get(1), false);
        assertThat(AopUtils.isCglibProxy(this.testUserService)).isTrue();
        assertThat(this.testUserService).isNotInstanceOf(java.lang.reflect.Proxy.class);
    }

    @Test
    @DisplayName("AspectJ 포인트컷 적용 테스트")
    public void upgradeAllOrNothingAspectJ() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            this.testUserService.upgradeLevels();
            fail("TestUserPolicyException expected");
        } catch (TestUserPolicyException e) {
            checkLevelUpgraded(users.get(1), false);
            assertThat(AopUtils.isJdkDynamicProxy(this.testUserService)).isTrue();
            assertThat(this.testUserService).isInstanceOf(java.lang.reflect.Proxy.class);
        }
    }

    @Test
    @DisplayName("읽기전용 메서드 테스트")
    public void readOnlyTransactionAttribute() {
        assertThrows(TransientDataAccessException.class, () -> {
            this.testUserService.getAll();
        });
    }
}