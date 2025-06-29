package mvc.model;

import enums.Difficulty;
import enums.Theme;
import interfaces.AbstractClue;
import interfaces.AbstractDecoration;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

public class Room extends Element {
    private int idEscapeRoom;
    private BigDecimal price;
    private Difficulty difficulty;
    private Theme theme;
    private List<AbstractClue> clues;
    private List<AbstractDecoration> decorations;
    private Boolean active;


}
