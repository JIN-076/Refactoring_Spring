package toby.spring.inha.refactor.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.dao.UserDaoJdbc;
import toby.spring.inha.refactor.user.domain.Level;
import toby.spring.inha.refactor.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Component
//@RequiredArgsConstructor
public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;

    private DataSource dataSource;

    @Autowired
    public UserService(UserDao userDao, UserLevelUpgradePolicy policy) {
        this.userDao = userDao;
        this.policy = policy;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void upgradeLevelsRfc() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (policy.canUpgradeLevel(user)) {
                policy.upgradeLevel(user);
            }
        }
    }

    public void upgradeLevelsRfc2() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(this.dataSource);
        c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (policy.canUpgradeLevel(user)) {
                    policy.upgradeLevel(user);
                }
            }
            c.commit();
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
