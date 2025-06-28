package mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SaleDetailsDTO {
    private int ticketId;
    private BigDecimal price;
    private String playerName;
}
