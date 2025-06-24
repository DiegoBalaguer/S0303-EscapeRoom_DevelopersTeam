package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class Reward extends Element {

    @Override
    public boolean isActive() {
        return super.active;
    }

    @Override
    public BigDecimal getValue() {
        return super.value;
    }


}
