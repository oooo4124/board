package hello.board.service;

import hello.board.domain.member.Member;
import hello.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member) {

        memberRepository.save(member);

        return member.getId();
    }
    //    중복회원 검사
    public String validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            return findMembers.get().getLoginId();
        }
        return null;
    }
    
    // 회원 전체 조회
    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    // memberId로 특정 회원 조회
    public Member findOneMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
