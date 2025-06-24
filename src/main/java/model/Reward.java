package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class Reward extends Element {


    @Override
    public BigDecimal getPrice() {
        return null;
    }
}
