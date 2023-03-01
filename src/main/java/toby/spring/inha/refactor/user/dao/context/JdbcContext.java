package toby.spring.inha.refactor.user.dao.context;

import lombok.NoArgsConstructor;
import toby.spring.inha.refactor.user.dao.strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor
public class JdbcContext {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 클라이언트로부터 StatementStrategy 타입의 전략 오브젝트를 제공받는다.
     * JDBC try/catch/finally 구조로 만들어진 컨텍스트 내 작업을 수행한다. -> DAO 메서드들이 공유하는 컨텍스트에 해당
     * @param strategy
     * @throws SQLException
     */

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        /**
         * 예외가 발생할 가능성이 있는 코드는 모두 try 블록으로 묶는다.
         * 예외가 발생했을 때 부가적인 작업을 해줄 수 있도록 catch 블록을 둔다. -> 예외 발생 시, 예외를 메서드 밖으로 던짐
         * try 블록의 예외 발생 여부와 관계 없이 실행될 finally 블록을 둔다.
         * 각 리소스의 null 여부를 확인하여, null 변수에 close() 메서드를 호출해 NullPointerException 되지 않게 한다.
         * close() 메서드에서도 SQLException 발생 가능성이 있음을 염두하자.
         */

        try {
            c = this.dataSource.getConnection();

            /**
             * 실제 전략, 즉 밖뛰는 부분인 PreparedStatement 부분을 StatementStrategy 인터페이스를 통해 분리
             * 삭제하는 SQL 실행을 수행하는 DeleteAllStatement 클래스가 이를 구현
             * 클라이언트가 아니기 때문에 그저 StatementStrategy 인터페이스를 통해 작업 수행 -> 구현 클래스 몰라도 돼~
             */

            ps = strategy.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

    public void executeSql(final String query, final String... varargs) throws SQLException {
        workWithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    int paramIdx = 1;

                    PreparedStatement ps = c.prepareStatement(query);
                    for (String arg : varargs) {
                        ps.setString(paramIdx++, arg);
                    }

                    return ps;
                }
            }
        );
    }

    public
}
