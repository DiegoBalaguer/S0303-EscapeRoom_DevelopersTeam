package mvc.entities.certificate;

import mvc.entities.Inputs;
import mvc.view.BaseView;
import utils.ConsoleUtils;

import java.util.Optional;

public class CertificateView {

    private static CertificateView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Certificate";


    private CertificateView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static CertificateView getInstance() {
        if (instance == null) {
            synchronized (CertificateView.class) {
                if (instance == null) {
                    instance = new CertificateView();
                }
            }
        }
        return instance;
    }

    protected Inputs getCertificateDetailsCreate() {
        return new Inputs.Builder()
                .put("name", getInputName().orElse(null))
                .put("description", getInputDescription().orElse(null))
                .build();
    }

    private Optional<String> getInputName() {
        return BASE_VIEW.getReadValueString("Enter name: ");
    }

    private Optional<String> getInputDescription() {
        return BASE_VIEW.getReadValueString("Enter description (30 chars): ");
    }

    protected Certificate getUpdateCertificateDetails(Certificate certificate) {
        certificate.setName(getUpdateName(certificate.getName()));
        certificate.setDescription(getUpdateDescription(certificate.getDescription()));
        certificate.setActive(getUpdateIsActive(certificate.isActive()));
        return certificate;
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

    protected void displayRecordCertificate(Certificate certificate) {
        String message = "";
        if (certificate != null) {
            message += BASE_VIEW.LINE + "--- " + NAME_OBJECT + " Details ---" + BASE_VIEW.LINE;
            message += "ID: " + certificate.getId() + BASE_VIEW.LINE;
            message += "Name: " + certificate.getName() + BASE_VIEW.LINE;
            message += "Description: " + certificate.getDescription() + BASE_VIEW.LINE;
            message += "Is Active: " + (certificate.isActive() ? "Yes" : "No") + BASE_VIEW.LINE;
            message += "-------------------------" + BASE_VIEW.LINE;
        } else {
            message = NAME_OBJECT + " not found.";
        }
        BASE_VIEW.displayMessageln(message);
    }
}
