package toby.spring.inha.refactor.user.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import toby.spring.inha.refactor.exception.DuplicateUserIdException;
import toby.spring.inha.refactor.user.dao.context.JdbcContext;
import toby.spring.inha.refactor.user.dao.strategy.StatementStrategy;
import toby.spring.inha.refactor.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao {

    private final DataSource dataSource;

    private final RowMapper<User> userRowMapper;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(DataSource dataSource, RowMapper<User> userRowMapper) {
        this.dataSource = dataSource;
        this.userRowMapper = userRowMapper;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public void add(final User user) throws DuplicateUserIdException {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userRowMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userRowMapper);
    }
}
