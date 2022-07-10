package com.tistory.jaimenote.jpa.orphan.infra.repository;

import com.tistory.jaimenote.jpa.orphan.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {

}