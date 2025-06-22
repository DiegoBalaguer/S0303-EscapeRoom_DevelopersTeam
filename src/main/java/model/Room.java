package model;

import enums.Difficulty;
import enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Room extends Element {
    private Difficulty difficulty;
    private Theme theme;

}
