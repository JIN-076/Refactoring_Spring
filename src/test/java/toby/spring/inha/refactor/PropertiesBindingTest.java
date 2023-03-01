package toby.spring.inha.refactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toby.spring.inha.refactor.properties.DataSourceProperties;

@SpringBootTest
public class PropertiesBindingTest {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Test
    @DisplayName("Properties Binding Test")
    public void PropertiesBinding() {
        Assertions.assertEquals(dataSourceProperties.getDriverClassName(), "com.mysql.cj.jdbc.Driver");
        Assertions.assertEquals(dataSourceProperties.getUrl(), "jdbc:mysql://localhost:3306/admin?serverTimeZone=Asia/Seoul");
        Assertions.assertEquals(dataSourceProperties.getUsername(), "dev");
        Assertions.assertEquals(dataSourceProperties.getPassword(), "password");
    }

    @Test
    @DisplayName("AssertJ Test")
    public void AssertJTest() {
        org.assertj.core.api.Assertions.assertThat(dataSourceProperties)
                .extracting("driverClassName").isEqualTo("com.mysql.cj.jdbc.Driver");
        org.assertj.core.api.Assertions.assertThat(dataSourceProperties)
                .extracting("url").isEqualTo("jdbc:mysql://localhost:3306/admin?serverTimeZone=Asia/Seoul");
        org.assertj.core.api.Assertions.assertThat(dataSourceProperties)
                .extracting("username").isEqualTo("dev");
        org.assertj.core.api.Assertions.assertThat(dataSourceProperties)
                .extracting("password").isEqualTo("password");
    }
}
