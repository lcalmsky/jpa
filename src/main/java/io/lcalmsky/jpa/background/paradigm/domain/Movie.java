package io.lcalmsky.jpa.background.paradigm.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Movie extends Content {
    private String director;
    private List<String> actors;
}
