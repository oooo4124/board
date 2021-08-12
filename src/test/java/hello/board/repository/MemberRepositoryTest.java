package hello.board.repository;

import hello.board.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void CRUD() {
        Member member1 = new Member("test1", "test", "test1");
        Member member2 = new Member("test2", "test", "test2");
        Member member3 = new Member("test3", "test", "test3");
        Member member4 = new Member("test4", "test", "test4");
        Member member5 = new Member("test5", "test", "test5");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        //단건 조회 id
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        assertThat(findMember1).isEqualTo(member1);

        //단건 조회 loginId
        Member findLoginMember = memberRepository.findByLoginId(member1.getLoginId()).get();
        assertThat(findLoginMember).isEqualTo(member1);


        //list 조회
        List<Member> findMembers = memberRepository.findAll();
        assertThat(findMembers.size()).isEqualTo(5);

        //count 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(5);

        //삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long delCount = memberRepository.count();
        assertThat(delCount).isEqualTo(3);


    }

}