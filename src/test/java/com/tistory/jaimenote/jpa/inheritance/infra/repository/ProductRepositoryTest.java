package com.tistory.jaimenote.jpa.inheritance.infra.repository;

import com.tistory.jaimenote.jpa.inheritance.entity.Product;
import com.tistory.jaimenote.jpa.inheritance.entity.Song;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
class ProductRepositoryTest {

  @Autowired
  ProductRepository productRepository;

  @PersistenceContext
  EntityManager entityManager;

  private Song song;

  @BeforeEach
  void setup() {
    song = Song.create("IU", "IU", "너의 의미", 500);
    productRepository.save(song);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @Rollback(false)
  void findProductTest() {
    Product product = productRepository.findById(song.getId())
        .orElseThrow(() -> new IllegalArgumentException("잘못된 ID 입니다."));
    System.out.println(product);
  }
}