package toby.spring.inha.refactor.user.service;

import toby.spring.inha.refactor.user.domain.User;

public interface UserService {

    void add(User user);
    void upgradeLevels();
}
