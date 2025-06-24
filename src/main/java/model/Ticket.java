package model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
public class Ticket extends Element {
    private BigDecimal price;
}
