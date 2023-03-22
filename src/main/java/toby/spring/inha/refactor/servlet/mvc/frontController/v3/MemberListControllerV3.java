package toby.spring.inha.refactor.servlet.mvc.frontController.v3;

import toby.spring.inha.refactor.servlet.domain.member.Member;
import toby.spring.inha.refactor.servlet.domain.repository.MemberRepository;
import toby.spring.inha.refactor.servlet.mvc.frontController.ModelView;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();

        ModelView modelView = new ModelView("members");
        modelView.getModel().put("members", members);
        return modelView;
    }
}
