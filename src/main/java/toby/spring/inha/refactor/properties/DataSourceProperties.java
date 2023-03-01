package toby.spring.inha.refactor.properties;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Setter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    @NotNull
    private final String driverClassName;
    @NotNull
    private final String url;
    @NotNull
    private final String username;
    @NotNull
    private final String password;
}
