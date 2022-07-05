package com.tistory.jaimenote.jpa.loading.infra.repository;

import com.tistory.jaimenote.jpa.loading.domain.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactoryRepository extends JpaRepository<Factory, Long> {

}