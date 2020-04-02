package io.lcalmsky.jpa.background.paradigm.domain.model;

import lombok.Data;

@Data
public class Player {
    private Long id;
    private String name;
    private Team team;
    private Agency agency;
}