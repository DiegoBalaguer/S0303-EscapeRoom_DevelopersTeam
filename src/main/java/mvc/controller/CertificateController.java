package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.CertificateDAO;
import mvc.model.Certificate;
import mvc.view.BaseView;
import mvc.view.CertificateView;

import java.util.List;
import java.util.Optional;

public class CertificateController {

    private final CertificateDAO CERTIFICATE_DAO;
    private BaseView baseView;
    private CertificateView certificateView;
    private static final String NAME_OBJECT = "Certificate";

    public CertificateController() {
        try {
            CERTIFICATE_DAO = DAOFactory.getDAOFactory().getCertificateDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        baseView = new BaseView();
        certificateView = new CertificateView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    // Queries for other Classes

    public int getCertificateIdWithList() {
        listAllCertificatesDetail();
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || CERTIFICATE_DAO.findById(searchID.get()).isEmpty()) {
            String message = "Certificate with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void listAllCertificatesDetail() throws DAOException {
        List<Certificate> certificates = CERTIFICATE_DAO.findAll();
        certificateView.displayListCertificates(certificates);
    }
}