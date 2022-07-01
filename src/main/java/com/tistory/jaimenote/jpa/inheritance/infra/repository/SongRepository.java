package com.tistory.jaimenote.jpa.inheritance.infra.repository;

import com.tistory.jaimenote.jpa.inheritance.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

}