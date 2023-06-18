package jpabook.jpashop.Repository;

import jpabook.jpashop.entity.Member;
import jpabook.jpashop.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}