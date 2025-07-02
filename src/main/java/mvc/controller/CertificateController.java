package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.CertificateDAO;
import mvc.enumsMenu.OptionsMenuCLFUSDE;
import mvc.model.Certificate;
import mvc.model.Reward;
import mvc.view.BaseView;
import mvc.view.CertificateView;

import java.util.List;
import java.util.Optional;

public class CertificateController {

    private static CertificateController certificateControllerInstance;
    private final CertificateDAO CERTIFICATE_DAO;
    private BaseView baseView;
    private CertificateView certificateView;
    private static final String NAME_OBJECT = "Certificate";

    public CertificateController() {
        baseView = new BaseView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
        try {
            CERTIFICATE_DAO = DAOFactory.getDAOFactory().getCertificateDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
       certificateView = new CertificateView();
    }

    public static CertificateController getInstance() {
        if (certificateControllerInstance == null) {
            synchronized (CertificateController.class) {
                if (certificateControllerInstance == null) {
                    certificateControllerInstance = new CertificateController();
                }
            }
        }
        return certificateControllerInstance;
    }

    public void mainMenu() {
        do {
            baseView.displayMessageln(OptionsMenuCLFUSDE.viewMenu(NAME_OBJECT.toUpperCase() + " MANAGEMENT"));
            int answer = baseView.getReadRequiredInt("Choose an option: ");
            OptionsMenuCLFUSDE idMenu = OptionsMenuCLFUSDE.getOptionByNumber(answer);
            try {
                switch (idMenu) {
                    case EXIT -> {
                        baseView.displayMessage2ln("Returning to Main Menu...");
                        return;
                    }
                    case CREATE -> createCertificate();
                    case LIST_ALL -> listAllCertificates();
                    case FIND_BY_ID -> getCertificateById();
                    case UPDATE -> updateCertificate();
                    case SOFT_DELETE -> softDeleteCertificateById();
                    case DELETE -> deleteCertificateById();

                    default -> baseView.displayErrorMessage("Error: The value in menu is wrong: " + idMenu);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                baseView.displayErrorMessage("Error: The value in menu is wrong." + e.getMessage());
            } catch (DAOException e) {
                baseView.displayErrorMessage("Error: Database operation failed: " + e.getMessage());
            } catch (Exception e) {
                baseView.displayErrorMessage("Error: An unexpected error occurred: " + e.getMessage());
            }
        } while (true);
    }


    private void createCertificate() {
        baseView.displayMessageln("#### CREATE " + NAME_OBJECT + "  #################");
        try {
            Certificate newCertificate = certificateView.getCertificateDetailsCreate();
            Certificate savedCertificate = CERTIFICATE_DAO.create(newCertificate);
            baseView.displayMessage2ln(NAME_OBJECT + " created successfully: " + savedCertificate.getName() + " (ID: " + savedCertificate.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error creating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void getCertificateById() {
        baseView.displayMessage2ln("####  GET " + NAME_OBJECT.toUpperCase() + " BY ID  #################");
        try {
            Optional<Certificate> optionalCertificate = CERTIFICATE_DAO.findById(getCertificateIdWithList());

            certificateView.displayRecordCertificate(optionalCertificate.get());
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error show " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void listAllCertificates() throws DAOException {
        baseView.displayMessage2ln("####  LIST ALL " + NAME_OBJECT.toUpperCase() + "S  #################");
        try {
            listAllCertificateDetail();
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error list all " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void updateCertificate() {
        baseView.displayMessage2ln("####  UPDATE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Certificate> existCertificateOpt = CERTIFICATE_DAO.findById(getCertificateIdWithList());

            baseView.displayMessage2ln("Current " + NAME_OBJECT + " Details:");
            certificateView.displayRecordCertificate(existCertificateOpt.get());

            baseView.displayMessage2ln("Enter new details:");
            baseView.displayMessageln("Enter new value or [INTRO] for not changes.");
            Certificate updatedCertificate = certificateView.getUpdateCertificateDetails(existCertificateOpt.get());

            Certificate savedCertificate = CERTIFICATE_DAO.update(updatedCertificate);
            baseView.displayMessage2ln(NAME_OBJECT + " updated successfully: " + savedCertificate.getName() + " (ID: " + savedCertificate.getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error updating " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void deleteCertificateById() {
        baseView.displayMessage2ln("####  DELETE " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            CERTIFICATE_DAO.deleteById(getCertificateIdWithList());
            baseView.displayMessage2ln(NAME_OBJECT + " deleted successfully (if existed).");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error deleting the " + NAME_OBJECT + ": " + e.getMessage());
        }
    }

    private void softDeleteCertificateById() throws DAOException {
        baseView.displayMessage2ln("#### SOFT DELETE  " + NAME_OBJECT.toUpperCase() + "  #################");
        try {
            Optional<Certificate> existCertificateOpt = CERTIFICATE_DAO.findById(getCertificateIdWithList());
            existCertificateOpt.get().setActive(false);

            CERTIFICATE_DAO.update(existCertificateOpt.get());
            baseView.displayMessage2ln(NAME_OBJECT + " soft deleted successfully: " + existCertificateOpt.get().getName() + " (ID: " + existCertificateOpt.get().getId() + ")");
        } catch (DAOException | IllegalArgumentException e) {
            baseView.displayErrorMessage("Error soft deleting " + NAME_OBJECT +": " + e.getMessage());
        }
    }

    // Queries for other Classes

    public int getCertificateIdWithList() {
        listAllCertificateDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || CERTIFICATE_DAO.findById(searchID.get()).isEmpty()) {
            String message = "Certificate with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllCertificateDetail() throws DAOException {
        List<Certificate> certificates = CERTIFICATE_DAO.findAll();
        certificateView.displayListCertificates(certificates);
    }
    public String getCertificateNameById(int rewardId) throws DAOException {
        return CERTIFICATE_DAO.findById(rewardId)
                .map(Certificate::getName)
                .orElse("Unknown Reward");
    }
}