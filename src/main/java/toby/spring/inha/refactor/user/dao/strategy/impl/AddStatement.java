package toby.spring.inha.refactor.user.dao.strategy.impl;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import toby.spring.inha.refactor.exception.DuplicateUserIdException;
import toby.spring.inha.refactor.user.dao.strategy.StatementStrategy;
import toby.spring.inha.refactor.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {

    private User user;

    public AddStatement(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws DuplicateUserIdException {
        try {
            PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
                throw new DuplicateUserIdException(e);
            else
                throw new RuntimeException(e);
        }
    }
}
