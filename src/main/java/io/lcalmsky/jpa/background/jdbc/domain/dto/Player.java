package io.lcalmsky.jpa.background.jdbc.domain.dto;

import lombok.Data;

@Data
public class Player {
    private Long id;
    private String name;
    private Integer goals;
    private Integer assists;
    private Integer dribbleTotalCount;
    private Integer dribbleSuccessCount;
}
