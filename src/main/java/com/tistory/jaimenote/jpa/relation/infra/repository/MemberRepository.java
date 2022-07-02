package com.tistory.jaimenote.jpa.relation.infra.repository;

import com.tistory.jaimenote.jpa.relation.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}