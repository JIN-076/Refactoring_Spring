package toby.spring.inha.refactor.servlet.mvc.frontController.v4;

import toby.spring.inha.refactor.servlet.domain.member.Member;
import toby.spring.inha.refactor.servlet.domain.repository.MemberRepository;

import java.util.List;
import java.util.Map;

public class MemberListControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {

        List<Member> members = memberRepository.findAll();
        model.put("members", members);

        return "members";
    }
}
