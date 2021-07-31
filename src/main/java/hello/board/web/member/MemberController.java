package hello.board.web.member;

import hello.board.domain.member.Member;
import hello.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String addForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/addMemberForm";
    }

    @PostMapping("/join")
    public String save(@Valid @ModelAttribute MemberForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }
        Member joinMember = new Member(form.getLoginId(), form.getPassword(), form.getName());

        String loginId = memberService.validateDuplicateMember(joinMember);

        if (loginId != null) {
            bindingResult.reject("joinFail","이미 존재하는 아이디입니다.");
            return "members/addMemberForm";
        }

        memberService.join(joinMember);

        return "redirect:/";
    }

}
