package hello.board.web;

import hello.board.domain.member.Member;
import hello.board.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitController {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Member testMember1 = new Member("tm1","1","tm1");
            Member testMember2 = new Member("tm2","1","tm2");
            em.persist(testMember1);
            em.persist(testMember2);

            for (int i = 0; i < 100; i++) {

                Member member = i % 2 == 0 ? testMember1 : testMember2;
                em.persist(new Post("테스트 데이터"+i,"테스트 데이터"+i, member, member.getUsername(),
                        LocalDateTime.now(),0,null));
            }
        }

    }
}
