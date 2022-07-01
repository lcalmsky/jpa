package com.tistory.jaimenote.jpa.inheritance.entity;

import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
public class Song extends Product {

  private String singer;

  private String writer;

  private Song(String singer, String writer, String name, Integer price) {
    this.singer = singer;
    this.writer = writer;
    setName(name);
    setPrice(price);
  }

  public static Song create(String singer, String writer, String name, Integer price) {
    return new Song(singer, writer, name, price);
  }
}
