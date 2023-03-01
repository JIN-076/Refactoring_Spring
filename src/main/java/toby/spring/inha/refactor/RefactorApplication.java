package toby.spring.inha.refactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import toby.spring.inha.refactor.properties.DataSourceProperties;

//@EnableConfigurationProperties(DataSourceProperties.class)
@SpringBootApplication
public class RefactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefactorApplication.class, args);
	}

}
