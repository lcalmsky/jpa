package com.tistory.jaimenote.jpa.infra.repository;

import com.tistory.jaimenote.jpa.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

}