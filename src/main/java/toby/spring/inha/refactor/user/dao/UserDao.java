package toby.spring.inha.refactor.user.dao;

import toby.spring.inha.refactor.user.domain.User;

import java.util.List;

public interface UserDao {

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
