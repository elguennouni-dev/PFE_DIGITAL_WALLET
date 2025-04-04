package pfe.digitalWallet.auth.jwt;

import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {

    @Value( "${jwt.secret.key}") // Hadi raha f app.prop
    private String SECRET_KEY;



}
