package toby.spring.inha.refactor.mvc.controller.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import toby.spring.inha.refactor.mvc.domain.HelloData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@Slf4j
@Controller
public class RequestParamController {

    /**
     * GET 쿼리 파라미터
     * http://localhost:8080/request-param-v1?username=hello&age=20
     */

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);

        response.getWriter().write("OK");
    }

    /**
     * @RequestParam: 파라미터 이름으로 바인딩.
     * String, int, Integer 등의 단순타입일 경우 @RequestParam 생략 가능
     *
     * @ResponseBody: View 조회를 무시하고, HTTP messageBody에 직접 해당 내용 입력
     */

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("userame") String memberName,
            @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "OK";
    }

    /**
     * @RequestParam.required: 파라미터 필수 여부
     * http://localhost:8080/request-param: username이 없으므로 400 예외 발생
     * http://localhost:8080/request-param?username=hello: 200 성공
     * http://localhost:8080/request-param?username=: 빈문자로 통과
     *
     * 기본형, primitive에 null 입력 불가
     * int: null 불가능. 500 예외 발생
     * Integer: null 가능
     * defaultValue: 미리 지정해둔 기본값 지정
     */

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) int age) {

        log.info("username={}, age={}", username, age);
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "OK";
    }

    /**
     * 파라미터 값이 1개일 경우 Map<String, Object> paramMap
     * 파라미터 값이 여러개일 경우 MultiValueMap<String, Object> paramMap
     */

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {

        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "OK";
    }

    /**
     * @ModelAttribute 사용
     * model.addAttribute(helloData) 코드도 자동 적용됨
     */

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "OK";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "OK";
    }


}
