package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@Data
@SuperBuilder
public abstract class Element {
    private int id;
    private String name;
    private String description;
    private boolean isActive;

    public Element() {
    }
    public abstract boolean isActive();
    public abstract BigDecimal getValue();

}
