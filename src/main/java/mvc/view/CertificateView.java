package mvc.view;

import mvc.dto.CertificateMapper;
import mvc.model.Certificate;
import utils.ConsoleUtils;
import utils.StringUtils;

import java.util.List;
import java.util.Optional;

public class CertificateView {

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Certificate";


    public Certificate getCertificateDetailsCreate() {
        try {
            return Certificate.builder()
                    .name(getInputName())
                    .description(getInputDescription())
                    .isActive(true)
                    .build();
        } catch (Exception e) {
            baseView.displayErrorMessage("Error collecting " + NAME_OBJECT + " details: " + e.getMessage());
            return null;
        }
    }

    private String getInputName() {
        return ConsoleUtils.readRequiredString("Enter name: ");
    }

    private String getInputDescription() {
        return ConsoleUtils.readRequiredString("Enter description: ");
    }

    public Certificate getUpdateCertificateDetails(Certificate certificate) {
        try {
            certificate.setName(getUpdateName(certificate.getName()));
            certificate.setDescription(getUpdateDescription(certificate.getDescription()));
            certificate.setActive(getUpdateIsActive(certificate.isActive()));
            return certificate;
        } catch (Exception e) {
            baseView.displayErrorMessage("Error editing " + NAME_OBJECT + ": " + e.getMessage());
            throw new IllegalArgumentException("Error editing " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private String getUpdateName(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter name: ", Optional.of(oldValue)).get();
    }

    private String getUpdateDescription(String oldValue) {
        return ConsoleUtils.readStringWithDefault("Enter description: ", Optional.of(oldValue)).get();
    }

    private Boolean getUpdateIsActive(boolean oldValue) {
        return ConsoleUtils.readBooleanWithDefault("Enter is active ('Y' or 'N'): ", Optional.of(oldValue)).get();
    }

    public void displayRecordCertificate(Certificate certificate) {
        String message = "";
        if (certificate != null) {
            message += baseView.LINE + "--- " + NAME_OBJECT + " Details ---" + baseView.LINE;
            message += "ID: " + certificate.getId() + baseView.LINE;
            message += "Name: " + certificate.getName() + baseView.LINE;
            message += "Description: " + certificate.getDescription() + baseView.LINE;
            message += "Is Active: " + (certificate.isActive() ? "Yes" : "No") + baseView.LINE;
            message += "-------------------------" + baseView.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        baseView.displayMessageln(message);
    }

    public void displayListCertificates(List<Certificate> certificates) {
        if (certificates.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificates.getFirst()).toListHead()));

        certificates.forEach(certificate -> baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificate).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
