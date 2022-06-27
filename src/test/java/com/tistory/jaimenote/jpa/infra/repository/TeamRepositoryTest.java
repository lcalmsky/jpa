package com.tistory.jaimenote.jpa.infra.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tistory.jaimenote.jpa.domain.entity.Member;
import com.tistory.jaimenote.jpa.domain.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class TeamRepositoryTest {

  @Autowired
  TeamRepository teamRepository;
  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("연관관계의 주인이 아닌 곳에서 업데이트를 해도 아무 변화 없음")
  @Rollback(false)
  void test() {
    Team team = Team.withName("토트넘");
    teamRepository.save(team);
    Member member = Member.withName("손흥민");
    team.getMembers().add(member);
    memberRepository.save(member);
    assertNull(member.getTeam());
    assertFalse(team.getMembers().isEmpty());
  }

  @Test
  @DisplayName("연관관계의 주인에도 동일하게 값을 추가해 줘야함")
  @Rollback(false)
  void test2() {

    Team team = Team.withName("토트넘");
    teamRepository.save(team);
    Member member = Member.withName("손흥민");
    member.join(team);
    memberRepository.save(member);
    assertNotNull(member.getTeam());
    assertFalse(team.getMembers().isEmpty());
  }
}