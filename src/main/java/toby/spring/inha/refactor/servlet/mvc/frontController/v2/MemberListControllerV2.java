package toby.spring.inha.refactor.servlet.mvc.frontController.v2;

import toby.spring.inha.refactor.servlet.domain.member.Member;
import toby.spring.inha.refactor.servlet.domain.repository.MemberRepository;
import toby.spring.inha.refactor.servlet.mvc.frontController.MyView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MemberListControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);

        return new MyView("/WEB-INF/views/members.jsp");
    }
}
