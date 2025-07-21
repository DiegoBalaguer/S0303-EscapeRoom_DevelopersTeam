package mvc.entities.certificateWin;

import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.view.BaseView;

import java.time.LocalDateTime;
import java.util.Optional;

public class CertificateWinView {

    private static CertificateWinView instance;
    private final BaseView BASE_VIEW;
    private static final String NAME_OBJECT = "Certificate Win";

    private CertificateWinView() {
        BASE_VIEW = BaseView.getInstance();
        BASE_VIEW.displayDebugMessage("Created Class: " + this.getClass().getName());
    }

    protected static CertificateWinView getInstance() {
        if (instance == null) {
            synchronized (CertificateWinView.class) {
                if (instance == null) {
                    instance = new CertificateWinView();
                }
            }
        }
        return instance;
    }

    protected Inputs getCertificateWinDetailsCreate(MessageMinMax playersList, MessageMinMax certificatesList, MessageMinMax roomsList) {

        Optional<Integer> playerId = getEntityId("List of Players", "Input Player ID: ", playersList);
        Optional<Integer> certificateId = getEntityId("List of Certificates", "Input Certificate ID: ", certificatesList);
        Optional<Integer> roomId = getEntityId("List of Rooms", "Input Room ID: ", roomsList);
        Optional<String> description = getInputDescription();

        return new Inputs.Builder()
                .put("playerId", playerId.orElse(null))
                .put("certificateId", certificateId.orElse(null))
                .put("roomId", roomId.orElse(null))
                .put("description", description.orElse(null))
                .put("dateDelivery", LocalDateTime.now())
                .build();
    }

    protected Optional<Integer> getEntityId(String title, String inputText, MessageMinMax messageMinMax) {
        return BASE_VIEW.getReadValueIntMinMax(title, inputText, messageMinMax);
    }

    private Optional<String> getInputDescription() {
        return BASE_VIEW.getReadValueString("Enter description (30 chars): ");
    }
}

