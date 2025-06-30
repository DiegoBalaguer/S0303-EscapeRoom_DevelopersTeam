package mvc.view;

import mvc.dto.CertificateMapper;
import mvc.model.Certificate;
import utils.StringUtils;

import java.util.List;

public class CertificateView {

    private static BaseView baseView;
    private static final String NAME_OBJECT = "Certificate";

    public CertificateView(){
        baseView = new BaseView();
    }


    public void displayListCertificates(List<Certificate> certificates) {
        if (certificates.isEmpty()) {
            baseView.displayMessageln("No " + NAME_OBJECT + "s found.");
            return;
        }
        baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificates.getFirst()).toListHead()));

        certificates.forEach(certificate -> baseView.displayMessageln(
                StringUtils.makeLineToList(CertificateMapper.toDisplayDTO(certificate).toList())));
        baseView.displayMessage2ln("-------------------");
    }
}
