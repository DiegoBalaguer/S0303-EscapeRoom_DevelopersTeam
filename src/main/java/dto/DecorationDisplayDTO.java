package dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DecorationDisplayDTO {
    private int id;
    private String name;
    private BigDecimal price;
    private int idRoom;
    private boolean isActive;
    private String description;

}