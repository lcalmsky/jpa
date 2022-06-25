package com.tistory.jaimenote.jpa.infra.repository;

import com.tistory.jaimenote.jpa.domain.entity.Member;
import com.tistory.jaimenote.jpa.domain.entity.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  TeamRepository teamRepository;
  @PersistenceContext
  EntityManager entityManager;

  @BeforeEach
  void setup() {
    Team team = Team.withName("토트넘");
    teamRepository.save(team);
    Member member = Member.withName("손흥민");
    member.join(team);
    memberRepository.save(member);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findMemberTest() {
    List<Member> members = memberRepository.findAll();
    for (Member member : members) {
      System.out.println(member);
    }
  }
}