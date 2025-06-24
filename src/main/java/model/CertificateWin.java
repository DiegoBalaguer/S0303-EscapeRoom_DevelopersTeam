package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CertificateWin {
    private int id;
    private int idCertificate;
    private int idPlayer;
    private int idRoom;
    private String description;
    private LocalDate dateDelivery;
    private boolean isActive;
}