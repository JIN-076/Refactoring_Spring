package toby.spring.inha.refactor.mvc.controller;

import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    /**
     * @Controller
     * @return String : view 이름으로 인식 -> view를 찾고 렌더링 수행
     */

    /**
     * @RestController
     * @return String : HTTP messageBody에 바로 입력
     */

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

        /**
         * @RequestMapping("/hello-basic")
         * @return String : /hello-basic url 호출이 오면 helloBasic()아 실행되도록 매핑
         *
         * 대부분의 속성을 []로 제공하므로 url 다중 설정도 가능하다.
         * ex) @RequestMapping({"/hello-basic", "/hello-go"})
         */

    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "OK";
    }

        /**
         * @RequestMapping
         * value: 요청받을 url 설정
         * method: HTTP 메서드 지정 { GET, HEAD, POST, PUT, PATCH, DELETE }
         *
         * 만약 지정한 HTTP 메서드가 아닌 다른 요청이 올 경우, Mvc는 HTTP 405 Method Not Allowed를 반환
         */

    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "OK";
    }

        /**
         * @GetMapping("/url")
         * HTTP 메서드를 축약한 애노테이션을 사용하는 것이 더 직관적
         * { @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping }
         */

    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "OK";
    }

        /**
         * @GetMapping path의 {} 안에 url에 사용될 변수명 입력: userId를 식별자로 사용
         * @PathVariable 이름 ("userId") 과 변수명 동일
         * @param data : @PathVariable 이름과 파라미터 이름이 동일할 경우, 생략 가능
         */

    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "OK";
    }

    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "OK";
    }

        /**
         * 특정 파라미터 조건 매핑 -> 파라미터로 추가 매핑
         *
         * params="mode"
         * params="!mode"
         * params="mode=debug"
         * params="mode != debug" (! = )
         * params = {"mode=debug", "data-good"}
         */

    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "OK";
    }

        /**
         * 특정 헤더 조건 매핑 -> 특정 헤더로 추가 매핑
         *
         * headers="mode",
         * headers="!mode"
         * headers="mode=debug"
         * headers="mode!=debug" ( ! = )
         */

    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "OK";
    }

        /**
         * 미디어 타입 조건 매핑 -> HTTP 요청 Content-Type, consume
         *
         * Content-Type 헤더 기반 추가 매핑 Media Type
         * consumes="application/json"
         * consumes="!application/json"
         * consumes="application/*"
         * consumes="*\/*"
         * MediaType.APPLICATION_JSON_VALUE
         *
         * HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입을 매핑한다.
         * 만약 맞지 않으면 HTTP 415 Unsupported Media Type을 반환
         */

    @PostMapping(value = "/mapping-consume", consumes = "applicatiion/json")
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "OK";
    }
}
