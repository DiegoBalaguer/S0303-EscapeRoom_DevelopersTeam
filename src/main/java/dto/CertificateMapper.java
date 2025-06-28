package dto;

import model.Certificate;
import model.Reward;

public class CertificateMapper {
    public static CertificateDisplayDTO toDisplayDTO(Certificate certificate) {
        return CertificateDisplayDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .isActive(certificate.isActive())
                .build();
    }
}
