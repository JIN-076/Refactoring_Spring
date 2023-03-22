package toby.spring.inha.refactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import toby.spring.inha.refactor.properties.DataSourceProperties;

/**
 * @ServletComponentScan
 * Servlet을 직접 등록해서 사용할 수 있도록 지원
 */

@ServletComponentScan
//@EnableConfigurationProperties(DataSourceProperties.class)
@SpringBootApplication
public class RefactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefactorApplication.class, args);
	}

}
