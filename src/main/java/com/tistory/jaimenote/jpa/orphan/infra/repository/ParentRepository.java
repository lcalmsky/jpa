package com.tistory.jaimenote.jpa.orphan.infra.repository;

import com.tistory.jaimenote.jpa.orphan.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {

}