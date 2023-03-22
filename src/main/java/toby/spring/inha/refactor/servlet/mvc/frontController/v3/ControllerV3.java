package toby.spring.inha.refactor.servlet.mvc.frontController.v3;

import toby.spring.inha.refactor.servlet.mvc.frontController.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
