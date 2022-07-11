//package com.tistory.jaimenote.jpa.domain;
//
//import com.tistory.jaimenote.jpa.inheritance.entity.Product;
//import com.tistory.jaimenote.jpa.inheritance.infra.repository.ProductRepository;
//import com.tistory.jaimenote.jpa.relation.domain.entity.Member;
//import com.tistory.jaimenote.jpa.relation.infra.repository.MemberRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//@DataJpaTest
//class BaseEntityTest {
//
//  @Autowired
//  MemberRepository memberRepository;
//
//  @Autowired
//  ProductRepository productRepository;
//
//  @Test
//  @Rollback(false)
//  void baseEntityTest() {
//    Member member = Member.create("name", "city", "street", "zipcode", null);
//    memberRepository.save(member);
//
//    Product product = Product.create("name", 1000);
//    productRepository.save(product);
//  }
//}