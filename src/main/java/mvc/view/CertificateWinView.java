package mvc.view;

import mvc.dto.CertificateWinDisplayDTO;
import utils.StringUtils;

import java.util.List;

public class CertificateWinView {

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Certificate Win";

    public CertificateWinView(){
        baseView = new BaseView();
    }


    public void displayListCertificateWinDTO(List<CertificateWinDisplayDTO> certificateWinDisplayDTOS) {
        if (certificateWinDisplayDTOS.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWinDisplayDTOS.getFirst().toListHead()));

        certificateWinDisplayDTOS.forEach(certificateWins -> baseView.displayMessageln(
                StringUtils.makeLineToList(certificateWins.toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
