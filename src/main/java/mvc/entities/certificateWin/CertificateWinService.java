package mvc.entities.certificateWin;

import dao.exceptions.DAOException;
import dao.exceptions.NotFoundException;
import mvc.entities.Inputs;
import mvc.entities.MessageMinMax;
import mvc.entities.certificate.CertificateService;
import mvc.entities.notification.NotificationService;
import mvc.entities.player.PlayerService;
import mvc.entities.room.RoomService;
import utils.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CertificateWinService {

    private final CertificateWinDAO CERTIFICATE_WIN_DAO;
    private static final String NAME_OBJECT = "Certificate Win";

    private final PlayerService PLAYER_SERVICE;
    private final RoomService ROOM_SERVICE;
    private final CertificateService CERTIFICATE_SERVICE;
    private final NotificationService NOTIFICATION_SERVICE;

    public CertificateWinService(
            CertificateWinDAO certificateWinDAO,
            PlayerService playerService,
            RoomService roomService,
            CertificateService certificateService,
            NotificationService notificationService) {
        this.CERTIFICATE_WIN_DAO = certificateWinDAO;
        this.PLAYER_SERVICE = playerService;
        this.ROOM_SERVICE = roomService;
        this.CERTIFICATE_SERVICE = certificateService;
        this.NOTIFICATION_SERVICE = notificationService;
    }

    protected CertificateWin createCertificateWin(Inputs inputsValue) throws IllegalArgumentException, DAOException, NotFoundException {

        Optional<Integer> playerId = inputsValue.getFieldAs("playerId", Integer.class);
        Optional<Integer> certificateId = inputsValue.getFieldAs("certificateId", Integer.class);
        Optional<Integer> roomId = inputsValue.getFieldAs("roomId", Integer.class);
        Optional<String> description = inputsValue.getFieldAs("description", String.class);
        Optional<LocalDateTime> dateDelivery = inputsValue.getFieldAs("dateDelivery", LocalDateTime.class);

        if (playerId.isEmpty() || PLAYER_SERVICE.getPlayerById(playerId.get()).isEmpty()) {
            throw new NotFoundException("Player with ID " + playerId.get() + " not found.");
        }
        if (roomId.isEmpty() || ROOM_SERVICE.getRoomById(roomId.get()).isEmpty()) {
            throw new NotFoundException("Room with ID " + roomId.get() + " not found.");
        }
        if (certificateId.isEmpty() || CERTIFICATE_SERVICE.getCertificateById(certificateId.get()).isEmpty()) {
            throw new NotFoundException("Certificate with ID " + certificateId.get() + " not found.");
        }

        CertificateWin newCertificateWin = CertificateWin.builder()
                .idPlayer(playerId.get())
                .idCertificate(certificateId.get())
                .idRoom(roomId.get())
                .dateDelivery(dateDelivery.get())
                .description(description.get())
                .isActive(true)
                .build();

        CertificateWin savedCertificateWin = CERTIFICATE_WIN_DAO.create(newCertificateWin);

        String playerName = PLAYER_SERVICE.getPlayerNameById(playerId.get());
        String certificateName = CERTIFICATE_SERVICE.getCertificateNameById(certificateId.get());
        Optional<String> message = ("Congratulations " + playerName + "! You have won the " + NAME_OBJECT + ": " + certificateName).describeConstable();
        NOTIFICATION_SERVICE.createNotification(playerId, message);

        return savedCertificateWin;
    }

    public MessageMinMax getAllCertificatesWinForPlayer(int playerId) throws NotFoundException {
            List<CertificateWinDisplayDTO> certificateWinList = CERTIFICATE_WIN_DAO.findByPlayerId(playerId);

            if (certificateWinList.isEmpty()) {
                throw new NotFoundException("No " + NAME_OBJECT + "s found.");
            }
            return new MessageMinMax(getDisplayListCertificatesWinDTO(certificateWinList), certificateWinList.size() > 0 ? 1 : 0, certificateWinList.size());
        }

    public String getDisplayListCertificatesWinDTO(List<CertificateWinDisplayDTO> certificateWinDisplayDTOS) {
        StringBuilder message = new StringBuilder();
        if (certificateWinDisplayDTOS.isEmpty()) {
            return "No " + NAME_OBJECT + "s found.";
        }
        message.append(
                StringUtils.makeLineToList(certificateWinDisplayDTOS.getFirst().toListHead())).append(System.lineSeparator());

        certificateWinDisplayDTOS.forEach(certificateWins -> message.append(
                StringUtils.makeLineToList(certificateWins.toList())).append(System.lineSeparator()));
        message.append("-------------------").append(System.lineSeparator());

        return message.toString();
    }

    public Optional<CertificateWin> getCertificateWinById(int id) throws DAOException, IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException(NAME_OBJECT + " ID must be positive.");
        }
        return CERTIFICATE_WIN_DAO.findById(id);
    }

    protected CertificateWin softDeleteCertificateWin(int id) throws NotFoundException, DAOException {
        Optional<CertificateWin> existingCertificateWinOpt = CERTIFICATE_WIN_DAO.findById(id);
        if (existingCertificateWinOpt.isEmpty()) {
            throw new NotFoundException(NAME_OBJECT + " with ID " + id + " not found for soft deletion.");
        }
        CertificateWin certificateWinToSoftDelete = existingCertificateWinOpt.get();
        certificateWinToSoftDelete.setActive(false);
        return CERTIFICATE_WIN_DAO.update(certificateWinToSoftDelete);
    }

    protected MessageMinMax getAllPlayers() {
        return PLAYER_SERVICE.getAllPlayers();
    }

    protected MessageMinMax getAllRooms() {
        return ROOM_SERVICE.getAllRooms();
    }

    protected MessageMinMax getAllCertificates() {
        return CERTIFICATE_SERVICE.getAllCertificates();
    }

}