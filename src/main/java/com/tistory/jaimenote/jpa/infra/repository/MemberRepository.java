package com.tistory.jaimenote.jpa.infra.repository;

import com.tistory.jaimenote.jpa.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}