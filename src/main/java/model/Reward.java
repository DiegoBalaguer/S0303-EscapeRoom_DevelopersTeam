package model;

import lombok.Data;

import java.math.BigDecimal;

@Data
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
