package com.tistory.jaimenote.jpa.inheritance.infra.repository;

import com.tistory.jaimenote.jpa.inheritance.entity.Song;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SongRepositoryTest {

  @Autowired
  SongRepository songRepository;

  @BeforeEach
  void setup() {
    Song song = Song.create("IU", "IU", "너의 의미", 500);
    songRepository.save(song);
  }

  @Test
  void findSongTest() {
    List<Song> songs = songRepository.findAll();
    for (Song song : songs) {
      System.out.println(song);
    }
  }
}