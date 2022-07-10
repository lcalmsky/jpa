package com.tistory.jaimenote.jpa.orphan.infra.repository;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.tistory.jaimenote.jpa.orphan.entity.Child;
import com.tistory.jaimenote.jpa.orphan.entity.Parent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class ParentRepositoryTest {

  private static final Long PARENT_ID = 1L;
  @Autowired
  ParentRepository parentRepository;
  @Autowired
  ChildRepository childRepository;
  @PersistenceContext
  EntityManager entityManager;

  @Test
  @Rollback(false)
  void removeOrphanTest() {
    Child child = Child.createChild();
    childRepository.save(child);

    Parent parent = Parent.createParent();
    parent.takeAsChild(child);
    parentRepository.save(parent);

    entityManager.flush();
    entityManager.clear();

    Parent foundParent = parentRepository.findById(parent.getId())
        .orElseThrow(IllegalAccessError::new);
    foundParent.getChildren().remove(0);

    entityManager.flush();
    entityManager.clear();

    Child foundChild = childRepository.findById(child.getId()).orElse(null);
    assertNull(foundChild);
  }
}