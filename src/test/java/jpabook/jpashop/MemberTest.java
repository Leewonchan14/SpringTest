package jpabook.jpashop;

import jpabook.jpashop.Repository.JPA.MemberJpaRepository;
import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.Repository.TeamRepository;
import jpabook.jpashop.dto.MemberDto;
import jpabook.jpashop.entity.Member;
import jpabook.jpashop.entity.Team;
import org.hibernate.annotations.NamedQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() throws Exception {
        Member member = new Member("memberA");
        Member save = memberRepository.save(member);
        Optional<Member> byId = memberRepository.findById(save.getId());
        Member findMember = byId.get();

        System.out.println("findMember = " + findMember);
    }

    @Test
    public void findBy(){
        Member m1 = new Member("AAA", 20);

        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC,
                "username"));

        Team aTeam = new Team("aTeam");
        teamRepository.save(aTeam);
        m1.changeTeam(aTeam);
        memberRepository.save(m1);

        Page<Member> page = memberRepository.findByUsername("AAA", pageRequest);

        List<Member> content = page.getContent(); //조회된 데이터
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?

    }


}