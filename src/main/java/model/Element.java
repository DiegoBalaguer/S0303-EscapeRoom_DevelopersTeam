package model;

import java.math.BigDecimal;

public abstract class Element {
    private int id;
    private String name;
    private String description;
    private BigDecimal value;
    private boolean active;

    public Element() {
    }
    public abstract boolean isActive();
    public abstract BigDecimal getValue();

}
