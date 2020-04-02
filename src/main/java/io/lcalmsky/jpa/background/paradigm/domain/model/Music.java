package io.lcalmsky.jpa.background.paradigm.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Music extends Content {
    private String singer;
    private String composer;
}
