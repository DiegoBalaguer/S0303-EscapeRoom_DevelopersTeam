package mvc.controller;

import dao.exceptions.DAOException;
import dao.exceptions.DatabaseConnectionException;
import dao.factory.DAOFactory;
import dao.interfaces.CertificateWinDAO;
import mvc.dto.CertificateWinDisplayDTO;
import mvc.model.CertificateWin;
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
        baseView = BaseView.getInstance();
        certificateWinView = new CertificateWinView();
        baseView.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    public int getCertificateWinForPlayerWithList(int playerId) {
        boolean status = listAllCertificatesWinForPlayerDetail(playerId);
        if (!status) {
            String message = "No " + NAME_OBJECT + "s found.";
            throw new IllegalArgumentException(message);
        }
        Optional<Integer> searchID = baseView.getReadValueInt("Enter " + NAME_OBJECT + " ID: ");
        if (searchID.isEmpty() || CERTIFICATEWIN_DAO.findById(searchID.get()).isEmpty()) {
            String message = NAME_OBJECT + " with ID required or not found.";
            baseView.displayErrorMessage(message);
            throw new IllegalArgumentException(message);
        }
        return searchID.get();
    }

    public boolean listAllCertificatesWinForPlayerDetail(int playerId) throws DAOException {
        List<CertificateWinDisplayDTO> certificateWinDisplayDTOS = getCertificateWinForPlayer(playerId);
        if(certificateWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return false;
        }
        certificateWinView.displayListCertificateWinDTO(certificateWinDisplayDTOS);
        return true;
    }

    public List<CertificateWinDisplayDTO> getCertificateWinForPlayer(int playerId) {
        return CERTIFICATEWIN_DAO.findByPlayerId(playerId);
    }

    public Optional<CertificateWin> getCertificateFindForId(int id) {
        return CERTIFICATEWIN_DAO.findById(id);
    }
}