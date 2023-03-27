package toby.spring.inha.refactor.mvc.controller.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import toby.spring.inha.refactor.mvc.domain.HelloData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @RestController: 해당 컨트롤러에 모두 @ResponseBody가 적용됨
 * 뷰 템플릿을 만드는게 아니라, HTTP 메시지 바디에 직접 데이터를 입력
 * 이름 그대로 Rest API를 만들 때 사용하는 컨트롤러
 */

@Slf4j
@Controller
//@RestController
public class ResponseBodyController {

    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {

        response.getWriter().write("OK");
    }

    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {

        return "OK";
    }

    /**
     * return ResponseEntity<>
     * HTTP 메시지 컨버터를 통해 JSON 형식으로 변환되어 반환됨
     */

    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {

        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    /**
     * @ResponseStatus: HTTP 응답 코드 설정
     * 응답 코드를 동적으로 변경할 수는 없음
     * 프로그램 조건에 따라 동적으로 변경하려면 ResponseEntity 사용할 것
     */

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {

        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
}
