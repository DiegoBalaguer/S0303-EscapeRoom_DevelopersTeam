package mvc.entities.certificate;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class CertificateService {


    private final CertificateDAO CERTIFICATE_DAO;
    private static final String NAME_OBJECT = "Certificate";

    public CertificateService(CertificateDAO certificateDAO) {
        this.CERTIFICATE_DAO = certificateDAO;
    }

    protected Certificate createCertificate(Inputs inputsValue) throws IllegalArgumentException, DAOException, NotFoundException {

        Optional<String> name = inputsValue.getFieldAs("name", String.class);
        Optional<String> description = inputsValue.getFieldAs("description", String.class);

        if (name.isEmpty()) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " name cannot be empty.");
        }

        Certificate certificate = Certificate.builder()
                .name(name.get())
                .description(description.get())
                .isActive(true)
                .build();
        return CERTIFICATE_DAO.create(certificate);
    }

    public Optional<Certificate> getCertificateById(int id) throws DAOException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return CERTIFICATE_DAO.findById(id);
    }

    public MessageMinMax getAllCertificates() throws DAOException {
        List<Certificate> certificates = CERTIFICATE_DAO.findAll();
        return new MessageMinMax(displayListCertificates(certificates), certificates.size() > 0 ? 1 : 0, certificates.size());
    }

    public Certificate updateCertificate(Certificate certificate) throws IllegalArgumentException, NotFoundException, DAOException {
        if (certificate == null || certificate.getId() <= 0) {
            throw new IllegalArgumentException("The " + NAME_OBJECT + " or its ID is not valid for the update.");
        }
        if (CERTIFICATE_DAO.findById(certificate.getId()).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + certificate.getId() + " not found for update.");
        }
        return CERTIFICATE_DAO.update(certificate);
    }

    public void deleteCertificate(int id) throws NotFoundException, DAOException {
        if (CERTIFICATE_DAO.findById(id).isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for deletion.");
        }
        CERTIFICATE_DAO.deleteById(id);
    }

    public Certificate softDeleteCertificate(int id) throws NotFoundException, DAOException {
        Optional<Certificate> existingCertificateOpt = CERTIFICATE_DAO.findById(id);
        if (existingCertificateOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        Certificate certificateToSoftDelete = existingCertificateOpt.get();
        certificateToSoftDelete.setActive(false);
        return CERTIFICATE_DAO.update(certificateToSoftDelete);
    }

    public String getCertificateNameById(int certificateId) throws DAOException {
        return CERTIFICATE_DAO.findById(certificateId)
                .map(Certificate::getName)
                .orElse("Unknown " + NAME_OBJECT);
    }

    private String displayListCertificates(List<Certificate> certificates) {
        StringBuilder message = new StringBuilder();
        if (certificates.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificates.getFirst()).toListHead())).append(System.lineSeparator());

        certificates.forEach(certificate -> message.append(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificate).toList())).append(System.lineSeparator()));

        message.append("-------------------").append(System.lineSeparator());
        return message.toString();
    }
}
