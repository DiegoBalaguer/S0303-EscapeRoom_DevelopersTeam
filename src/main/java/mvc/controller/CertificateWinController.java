package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.CertificateWinDAO;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.view.BaseView;
import mvc.view.CertificateWinView;

import java.util.List;
import java.util.Optional;

public class CertificateWinController {

    private final CertificateWinDAO CERTIFICATEWIN_DAO;
    private BaseView baseView;
    private CertificateWinView certificateWinView;
    private static final String NAME_OBJECT = "Certificate Win";

    public CertificateWinController() {

        try {
            CERTIFICATEWIN_DAO = DAOFactory.getDAOFactory().getCertificateWinDAO();
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        baseView = new BaseView();
        certificateWinView = new CertificateWinView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }


    public int getCertificateWinForPlayerWithList(int playerId) {
        ListAllCertificatesWinForPlayerDetail(playerId);
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || CERTIFICATEWIN_DAO.findByPlayerId(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    private void ListAllCertificatesWinForPlayerDetail(int playerId) throws DAOException {
        List<CertificateWinDisplayDTO> certificateWinDisplayDTOS = CERTIFICATEWIN_DAO.findByPlayerId(playerId);
        certificateWinView.displayListCertificateWinDTO(certificateWinDisplayDTOS);
    }


}