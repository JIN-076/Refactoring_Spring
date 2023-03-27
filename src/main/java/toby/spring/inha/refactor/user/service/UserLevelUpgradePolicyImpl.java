package toby.spring.inha.refactor.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.domain.Level;
import toby.spring.inha.refactor.user.domain.User;

import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static toby.spring.inha.refactor.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@Component
public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy {

    private final UserDao userDao;

    private final EmailPolicy emailPolicy;

    @Autowired
    public UserLevelUpgradePolicyImpl(UserDao userDao, EmailPolicy emailPolicy) {
        this.userDao = userDao;
        this.emailPolicy = emailPolicy;
    }

    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        emailPolicy.sendUpgradeEmailRfc(user);
    }
}
