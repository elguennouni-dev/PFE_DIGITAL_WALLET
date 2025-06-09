package pfe.digitalWallet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pfe.digitalWallet.QrWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final QrWebSocketHandler qrWebSocketHandler;

    public void confirmQrSession(String qrCodeData, String token) {
        qrWebSocketHandler.sendToken(qrCodeData, token);
    }

    @Autowired
    public WebSocketConfig(QrWebSocketHandler qrWebSocketHandler) {
        this.qrWebSocketHandler = qrWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(qrWebSocketHandler, "/ws/qr")
                .setAllowedOrigins("http://localhost:3000");
    }
}
