package toby.spring.inha.refactor.user.service;

import toby.spring.inha.refactor.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
