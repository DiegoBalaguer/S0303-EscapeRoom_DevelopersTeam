package model;

import enums.Difficulty;
import enums.Theme;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Room extends Element {
    private int idEscapeRoom;
    private BigDecimal price;
    private Difficulty difficulty;
    private Theme theme;

    @Override
    public BigDecimal getPrice() {
        return null;
    }
}
