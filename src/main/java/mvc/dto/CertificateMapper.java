package mvc.dto;

import mvc.model.Certificate;

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
