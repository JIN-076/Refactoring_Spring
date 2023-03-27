package toby.spring.inha.refactor.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import toby.spring.inha.refactor.config.DataSourceConfig;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ComponentScan(
        basePackages = {"toby.spring.inha.refactor"},
        basePackageClasses = UserServiceImpl.class
)
@ContextConfiguration(classes = {EmailPolicy.class, MailSenderConfig.class, TransactionConfig.class, UserServiceImpl.class, UserLevelUpgradePolicyImpl.class, UserDaoJdbc.class, DataSourceConfig.class, UserMapper.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

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
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao, userLevelUpgradePolicy);

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
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao, userLevelUpgradePolicy);
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
        UserServiceImpl testUserService = new UserServiceImpl(this.userDao, userLevelUpgradePolicy);
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

        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao, userLevelUpgradePolicy);

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
}
