package toby.spring.inha.refactor.mvc.controller.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("OK");
    }

    /**
     * 스프링 MVC는 다음 파라미터를 지원
     * @param InputStream (Reader): HTTP 요청 메시지바디 내용을 직접 조회
     * @param OutputStream (Writer): HTTP 응답 메시지바디에 직접 결과 출력
     */

    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("OK");
    }

    /**
     * 스프링 MVC는 다음 파라미터를 지원
     * @param httpEntity
     *
     * HTTP 헤더, 바디 정보를 편리하게 조회
     * 1. 메시지 바디 정보를 직접 조회
     * 2. 요청 파라미터를 조회하는 기능과 관계 없다 -> @RequestParam X, @ModelAttribute X
     *
     * HttpEntity는 응답에도 사용 가능
     * 1. 메시지 바디 정보 직접 반환
     * 2. 헤더 정보 포함 가능
     * 3. 뷰 조회 X
     */

    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<>("OK");
    }

    /**
     * @RequestBody
     * HTTP 메시지바디 정보를 편리하게 조회할 수 있다.
     * 헤더 정보가 필요하다면 -> HttpEntity, @RequestHeader 사용
     */

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {

        log.info("messageBody={}", messageBody);
        return "OK";
    }
}
