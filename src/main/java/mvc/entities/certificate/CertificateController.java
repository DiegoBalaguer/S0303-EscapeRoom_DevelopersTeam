package mvc.entities.certificate;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.view.BaseView;

import java.util.Optional;

public class CertificateController {

    private static CertificateController instance;
    private final CertificateService CERTIFICATE_SERVICE;
    private final CertificateView CERTIFICATE_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Certificate";

    private CertificateController(CertificateService certificateService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        CERTIFICATE_VIEW = CertificateView.getInstance();
        this.CERTIFICATE_SERVICE = certificateService;
    }

    public static CertificateController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (CertificateController.class) {
                if (instance == null) {
                    CertificateService service = ServiceManager.getInstance().getCertificateService();
                    instance = new CertificateController(service);
                }
            }
        }
        return instance;
    }

    public void mainMenu() {
        do {
            BASE_VIEW.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = BASE_VIEW.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        BASE_VIEW.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createCertificate();
                    case LIST_ALL -> listAllCertificates();
                    case FIND_BY_ID -> getCertificateById();
                    case UPDATE -> updateCertificate();
                    case SOFT_DELETE -> softDeleteCertificateById();
                    case DELETE -> deleteCertificateById();

                    default -> BASE_VIEW.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException e) {
                BASE_VIEW.displayErrorMessage("Error: Invalid data entered. " + e.getMessage());
            } catch (DAOException e) {
                BASE_VIEW.displayErrorMessage("Error: Database operation failed. " + e.getMessage());
            } catch (NullPointerException e) {
                BASE_VIEW.displayErrorMessage("An unexpected error occurred (Null Pointer): " + e.getMessage());
            } catch (NotFoundException e) {
                BASE_VIEW.displayErrorMessage("Entity not found: " + e.getMessage());
            }
        } while (true);
    }

    private void createCertificate() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");

        Inputs valuesInput = CERTIFICATE_VIEW.getCertificateDetailsCreate();
        CERTIFICATE_SERVICE.createCertificate(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    private void getCertificateById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        Optional<Integer> searchIdOpt = getCertificateIdWithList();
        Optional<Certificate> optionalCertificate = CERTIFICATE_SERVICE.getCertificateById(searchIdOpt.get());
        CERTIFICATE_VIEW.displayRecordCertificate(optionalCertificate.get());
    }

    private void listAllCertificates() throws DAOException {
        BASE_VIEW.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        BASE_VIEW.displayMessageln(CERTIFICATE_SERVICE.getAllCertificates().getMessage());
    }

    private void updateCertificate() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getCertificateIdWithList();
        Optional<Certificate> existCertificateOpt = CERTIFICATE_SERVICE.getCertificateById(searchIdOpt.get());
        if (existCertificateOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found for update.";
            BASE_VIEW.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        BASE_VIEW.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
        CERTIFICATE_VIEW.displayRecordCertificate(existCertificateOpt.get());

        BASE_VIEW.displayMessageln("Enter new details:");
        BASE_VIEW.displayMessageln("Enter new value or [INTRO] for no changes.");
        Certificate updatedCertificate = CERTIFICATE_VIEW.getUpdateCertificateDetails(existCertificateOpt.get());

        CERTIFICATE_SERVICE.updateCertificate(updatedCertificate);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " updated successfully.");
    }

    private void deleteCertificateById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getCertificateIdWithList();
        CERTIFICATE_SERVICE.deleteCertificate(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " deleted successfully.");
    }

    private void softDeleteCertificateById() throws NotFoundException {
        BASE_VIEW.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        Optional<Integer> searchIdOpt = getCertificateIdWithList();
        CERTIFICATE_SERVICE.softDeleteCertificate(searchIdOpt.get());
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " soft deleted successfully.");
    }

    private Optional<Integer> getCertificateIdWithList() throws NotFoundException {
        Optional<Integer> searchIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Certificates", "Input Certificate ID: ",CERTIFICATE_SERVICE.getAllCertificates());
        if (searchIdOpt.isEmpty()) {
            String message = NAME_OBJECT + " not found.";
            BASE_VIEW.displayErrorMessage(message);
            throw new NotFoundException(message);
        }
        return  searchIdOpt;
    }
}