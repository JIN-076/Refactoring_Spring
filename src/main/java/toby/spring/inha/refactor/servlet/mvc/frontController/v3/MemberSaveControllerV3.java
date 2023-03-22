package toby.spring.inha.refactor.servlet.mvc.frontController.v3;

import toby.spring.inha.refactor.servlet.domain.member.Member;
import toby.spring.inha.refactor.servlet.domain.repository.MemberRepository;
import toby.spring.inha.refactor.servlet.mvc.frontController.ModelView;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView modelView = new ModelView("save-result");
        modelView.getModel().put("member", member);
        return modelView;
    }
}
