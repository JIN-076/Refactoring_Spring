package toby.spring.inha.refactor.user;

import org.apache.el.parser.AstSetData;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toby.spring.inha.refactor.config.DataSourceConfig;
import toby.spring.inha.refactor.ctx.AppCtx;
import toby.spring.inha.refactor.user.dao.UserDao;
import toby.spring.inha.refactor.user.domain.User;

import java.sql.SQLException;

/**
 * @Test JUnit -> @Test 메서드를 실행할 때마다 새로운 오브젝트를 생성
 * JUnit 확장 기능은 테스트가 실행되기 전에 딱 한번만 ApplicationContext 생성.
 * 그 후에 @Test 메서드가 만들어질 때마다 특별한 방법을 이용해 ApplicationContext 자신을 오브젝트의 특정 필드에 주입.
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppCtx.class, DataSourceConfig.class})
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;
    private User userA;
    private User userB;
    private User userC;

    @BeforeEach
    public void setUp() {
        this.userA = new User("1234", "이유노", "yuNoh");
        this.userB = new User("1235", "안유정", "tanGuRae");
        this.userC = new User("1236", "라찬엽", "raDoll");
    }

    @Test
    @DisplayName("addAndGet() 테스트")
    public void addAndGet() throws SQLException {

        dao.deleteAll();
        Assertions.assertEquals(dao.getCount(), 0);

        dao.add(userA);
        dao.add(userB);
        Assertions.assertEquals(dao.getCount(), 2);

        User userGetA = dao.get(userA.getId());
        Assertions.assertEquals(userGetA.getName(), userA.getName());
        Assertions.assertEquals(userGetA.getPassword(), userA.getPassword());

        User userGetB = dao.get(userB.getId());
        Assertions.assertEquals(userGetB.getName(), userB.getName());
        Assertions.assertEquals(userGetB.getPassword(), userB.getPassword());
    }

    @Test
    @DisplayName("getCount() 테스트")
    public void count() throws SQLException {

        dao.deleteAll();
        Assertions.assertEquals(dao.getCount(), 0);

        dao.add(userA);
        Assertions.assertEquals(dao.getCount(), 1);

        dao.add(userB);
        Assertions.assertEquals(dao.getCount(), 2);

        dao.add(userC);
        Assertions.assertEquals(dao.getCount(), 3);
    }

    /**
     * Junit4의 경우, @Test(EmptyResultDataAccessException.class)를 사용
     */

    @Test
    @DisplayName("존재하지 않는 ID 예외 처리 테스트")
    public void getUserFailure() throws SQLException {

        dao.deleteAll();
        Assertions.assertEquals(dao.getCount(), 0);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });
    }
}
