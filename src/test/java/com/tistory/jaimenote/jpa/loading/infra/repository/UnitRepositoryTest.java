package com.tistory.jaimenote.jpa.loading.infra.repository;

import com.tistory.jaimenote.jpa.loading.domain.entity.Factory;
import com.tistory.jaimenote.jpa.loading.domain.entity.Unit;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class UnitRepositoryTest {

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  FactoryRepository factoryRepository;

  @PersistenceContext
  EntityManager entityManager;

  private Unit marine;

  @BeforeEach
  void setup() {
    Factory barracks = Factory.create("barracks");
    factoryRepository.save(barracks);
    marine = Unit.create("marine", barracks);
    unitRepository.save(marine);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @Rollback(false)
  void findMember() {
    unitRepository.findById(marine.getId());
  }

  @Test
  void findMemberAndTeam() {
    unitRepository.findById(marine.getId())
        .map(Unit::getFactory)
        .ifPresent(System.out::println);
  }
}