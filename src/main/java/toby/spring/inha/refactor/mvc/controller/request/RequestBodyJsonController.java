package toby.spring.inha.refactor.mvc.controller.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import toby.spring.inha.refactor.mvc.domain.HelloData;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);

        /**
         * 문자로 된 JSON 데이터를 Jackson 라이브러리 objectMapper를 사용해 자바 객체로 변환
         */

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        response.getWriter().write("OK");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "OK";
    }

    /**
     * HttpEntity, @RequestBody 애노테이션을 사용하면 HTTP 메시지 컨버터가 HTTP 메시지바디 내용을 원하는 문자나 객체로 변환해준다.
     * HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON 형식도 객체도 변환해준다.
     * HTTP 요청 메세지를 통해 전달받은 JSON 형식을 String 객체로 변환 -> @RequestBody String messageBody
     * HTTP 요청 메시지를 통해 전달받은 JSON 형식을 HelloData 객체로 변환 -> @RequestBody HelloData helloData
     */

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "OK";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {

        HelloData helloData = httpEntity.getBody();
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "OK";
    }

    /**
     * @RequestBody 요청: JSON 요청 -> HTTP 메시지 컨버터 -> 오브젝트
     * @RequestBody 응답: 오브젝트 -> HTTP 메시지 컨버터 -> JSON 응답
     */

    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return helloData;
    }
}
