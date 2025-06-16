package pfe.digitalWallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pfe.digitalWallet.auth.jwt.JwtUtil;
import pfe.digitalWallet.core.appuser.AppUser;
import pfe.digitalWallet.core.appuser.UserService;
import pfe.digitalWallet.exception.UnauthorizedException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QrWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String qrCodeData = getQrCodeDataFromQuery(session.getUri());
        if (qrCodeData != null) {
            sessions.put(qrCodeData, session);
        } else {
            session.close(CloseStatus.BAD_DATA.withReason("Missing qrCodeData parameter"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.values().remove(session);
    }

    public void sendToken(String qrCodeData, String token) {
        WebSocketSession session = sessions.get(qrCodeData);
        if (session != null && session.isOpen()) {
            try {
                String username = jwtUtil.getUsernameFromToken(token);
                AppUser user = userService.getByUsername(username)
                        .orElseThrow(() -> new UnauthorizedException("JWT username not found"));
                String jsonMessage = String.format(
                        "{\"type\":\"token\", \"data\": {\"id\": \"%d\", \"username\": \"%s\", \"email\": \"%s\", \"token\": \"%s\"}}",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        token
                );
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String qrCodeData = message.getPayload();
        sessions.put(qrCodeData, session);
    }


    private String getQrCodeDataFromQuery(URI uri) {
        if (uri == null || uri.getQuery() == null) return null;
        for (String param : uri.getQuery().split("&")) {
            String[] keyVal = param.split("=");
            if (keyVal.length == 2 && keyVal[0].equals("qrCodeData")) {
                return keyVal[1];
            }
        }
        return null;
    }

}
