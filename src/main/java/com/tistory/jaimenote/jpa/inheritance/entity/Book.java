package com.tistory.jaimenote.jpa.inheritance.entity;

import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Book extends Product {

  private String author;

  private String isbn;

}
