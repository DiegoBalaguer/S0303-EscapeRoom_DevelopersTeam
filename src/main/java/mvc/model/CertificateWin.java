package mvc.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class CertificateWin {
    private int id;
    private int idCertificate;
    private int idPlayer;
    private int idRoom;
    private String description;
    private LocalDateTime dateDelivery;
    private boolean isActive;
}