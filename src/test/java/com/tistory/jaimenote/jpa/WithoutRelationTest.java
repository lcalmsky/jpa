package com.tistory.jaimenote.jpa;

import com.tistory.jaimenote.jpa.domain.entity.Member;
import com.tistory.jaimenote.jpa.domain.entity.Team;
import com.tistory.jaimenote.jpa.infra.repository.MemberRepository;
import com.tistory.jaimenote.jpa.infra.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
public class WithoutRelationTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  TeamRepository teamRepository;

  @BeforeEach
  void setup() {
    Team team = Team.withName("토트넘");
    teamRepository.save(team);
    Member member = Member.withName("손흥민").withTeamId(team.getId());
    memberRepository.save(member);
  }

  @Test
  @Rollback(false)
  void findTest() {
    Member member = memberRepository.findById(2L)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID"));
    Team team = teamRepository.findById(member.getTeamId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID"));
  }
}