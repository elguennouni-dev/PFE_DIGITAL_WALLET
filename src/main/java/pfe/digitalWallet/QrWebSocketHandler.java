package pfe.digitalWallet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QrWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String qrCodeData = getQrCodeDataFromQuery(session.getUri());
        if (qrCodeData != null) {
            sessions.put(qrCodeData, session);
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
                session.sendMessage(new TextMessage(token));
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

//    public void sendToken(String qrCodeData, String token) {
//        WebSocketSession clientSession = sessions.get(qrCodeData);
//        if (clientSession != null && clientSession.isOpen()) {
//            try {
//                clientSession.sendMessage(new TextMessage(token));
//                clientSession.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.values().removeIf(ws -> ws.equals(session));
//    }
}
