package io.lcalmskyl.jpa.background.paradigm.domain;

import io.lcalmsky.jpa.background.paradigm.domain.model.Book;
import io.lcalmsky.jpa.background.paradigm.domain.model.Content;
import io.lcalmsky.jpa.background.paradigm.domain.model.Music;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContentCollectionTests {

    private static List<Content> contents;

    @BeforeAll
    public static void setup() {
        contents = new ArrayList<>();
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("jaime");
        book.setPublisher("tistory");
        book.setName("jaime-note");
        book.setPrice(0);

        contents.add(book);
    }

    @Test
    @DisplayName("Content 컬렉션에 Music 객체 저장")
    public void givenContents_whenSaveIntoCollection_thenSaveSuccess() {
        Music anysong = new Music();
        anysong.setComposer("zico");
        anysong.setSinger("zico");
        anysong.setId(2L);
        anysong.setName("anysong");
        anysong.setPrice(500);
        contents.add(anysong);

        assertEquals(contents.size(), 2);
    }

    @Test
    @DisplayName("Content 컬렉션에서 Book 객체 찾기")
    public void givenCollection_whenRetrieveContents_thenFindSuccessfully() {
        assertTrue(contents.stream()
                .anyMatch(c -> c.getName().equals("jaime-note")));
    }
}
