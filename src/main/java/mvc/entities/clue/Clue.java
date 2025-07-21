package mvc.entities.clue;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import mvc.model.Element;

import java.math.BigDecimal;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

public class Clue extends Element {
    private int idRoom;
    private BigDecimal price;
    private String description;

}
