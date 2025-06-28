package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.Getter;

import java.math.BigDecimal;
@Data
@SuperBuilder
@Getter

public abstract class Element{
    private int id;
    private String name;
    private String description;
    private boolean isActive;


    protected Element(ElementBuilder<?, ?> builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

}
