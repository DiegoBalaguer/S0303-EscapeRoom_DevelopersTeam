package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class Clue extends Element {
    private int idRoom;
    private BigDecimal price;

}
