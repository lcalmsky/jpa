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
public class Movie extends Product {

  private String director;

  private String actor;

}
