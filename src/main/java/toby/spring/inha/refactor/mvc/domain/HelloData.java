package toby.spring.inha.refactor.mvc.domain;

import lombok.Data;

/**
 * @Data
 * @Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor 자동 지원
 */

@Data
public class HelloData {

    private String username;
    private int age;
}
