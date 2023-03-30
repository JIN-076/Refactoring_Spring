package toby.spring.inha.refactor.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.domain.Level;
import toby.spring.inha.refactor.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final UserDao userDao;
    private UserLevelUpgradePolicy policy;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setPolicy(UserLevelUpgradePolicy policy) {
        this.policy = policy;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (policy.canUpgradeLevel(user)) {
                policy.upgradeLevel(user);
            }
        }
    }
}
