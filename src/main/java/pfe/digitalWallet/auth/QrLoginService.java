package pfe.digitalWallet.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.digitalWallet.QrWebSocketHandler;

@Service
@RequiredArgsConstructor
public class QrLoginService {

    @Autowired
    private QrWebSocketHandler qrWebSocketHandler;

}
