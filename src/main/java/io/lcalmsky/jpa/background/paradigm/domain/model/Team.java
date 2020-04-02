package io.lcalmsky.jpa.background.paradigm.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Team {
    private Long id;
    private String name;
    private String country;
    private League league;
    private List<Staff> staffs;
}
