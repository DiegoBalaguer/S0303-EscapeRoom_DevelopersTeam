package mvc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
        private int id;
        private int idTicket;
        private int idPlayer;
        private int idRoom;
        private int players;
        private BigDecimal price;
        private int completion;
        private LocalDateTime dateSale;
        private boolean isActive;
}
