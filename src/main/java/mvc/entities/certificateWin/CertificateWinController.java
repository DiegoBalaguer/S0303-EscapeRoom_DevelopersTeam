package mvc.entities.certificateWin;

import dao.exceptions.DatabaseConnectionException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.ServiceManager;
import mvc.view.BaseView;

import java.util.Optional;

public class CertificateWinController {

    private static CertificateWinController instance;
    private final CertificateWinService CERTIFICATE_WIN_SERVICE;
    private final CertificateWinView CERTIFICATE_WIN_VIEW;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Certificate Win";


    private CertificateWinController(CertificateWinService certificateWinService) {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
        this.CERTIFICATE_WIN_SERVICE = certificateWinService;
        CERTIFICATE_WIN_VIEW = CertificateWinView.getInstance();
    }

    public static CertificateWinController getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            synchronized (CertificateWinController.class) {
                if (instance == null) {
                    CertificateWinService service = ServiceManager.getInstance().getCertificateWinService();
                    instance = new CertificateWinController(service);
                }
            }
        }
        return instance;
    }

    public void createCertificateWin() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### AWARD  " + NAME_OBJECT.toUpperCase() + " TO PLAYER  #################");

        Inputs valuesInput = CERTIFICATE_WIN_VIEW.getCertificateWinDetailsCreate(
                CERTIFICATE_WIN_SERVICE.getAllPlayers(),
                CERTIFICATE_WIN_SERVICE.getAllRooms(),
                CERTIFICATE_WIN_SERVICE.getAllCertificates());
        CERTIFICATE_WIN_SERVICE.createCertificateWin(valuesInput);
        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " added successfully.");
    }

    public void softDeleteCertificateWinById() throws NotFoundException {
        BASE_VIEW.displayMessageln("#### REVOKE  " + NAME_OBJECT.toUpperCase() + " TO PLAYER  #################");

        Optional<Integer> playerIdOpt = getPlayerId();

        Optional<Integer> certificateWinId =
                BASE_VIEW.getReadValueIntMinMax("Certificate Win For Player", "Input Certificate Win ID: ", CERTIFICATE_WIN_SERVICE.getAllCertificatesWinForPlayer(playerIdOpt.get()));

        CertificateWin softDeletedCertificateWin = CERTIFICATE_WIN_SERVICE.softDeleteCertificateWin(certificateWinId.get());

        BASE_VIEW.displayMessage2ln(NAME_OBJECT + " Player unsubscribed successfully: " + softDeletedCertificateWin.getId());
    }

    private Optional<Integer> getPlayerId() throws NotFoundException {
        Optional<Integer> playerIdOpt = BASE_VIEW.getReadValueIntMinMax("List of Players", "Input Player ID: ", CERTIFICATE_WIN_SERVICE.getAllPlayers());
        if (playerIdOpt.isEmpty()) {
            String message = "No Player found for " + NAME_OBJECT + "s.";
            BASE_VIEW.displayMessageln(message);
            throw new NotFoundException(message);
        }
        return playerIdOpt;
    }
}
