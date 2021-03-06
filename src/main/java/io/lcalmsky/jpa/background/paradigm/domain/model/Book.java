package io.lcalmsky.jpa.background.paradigm.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends Content {
    private String author;
    private String publisher;
}
