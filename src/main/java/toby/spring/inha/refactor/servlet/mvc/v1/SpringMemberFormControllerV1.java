package toby.spring.inha.refactor.servlet.mvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * RequestMappingHandlerMapping
 * Bean 중에 @RequestMapping · @Controller가 클래스 레벨에 붙어 있는 경우 매핑 정보로 인식한다.
 */

@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }
}
