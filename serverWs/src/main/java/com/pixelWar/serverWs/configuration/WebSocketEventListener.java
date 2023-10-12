package com.pixelWar.serverWs.configuration;

import com.pixelWar.serverWs.entity.EMessageType;
import com.pixelWar.serverWs.entity.PixelDrawing;
import com.pixelWar.serverWs.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageSendingOperations;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String) headerAccessor.getSessionAttributes().get("username");
        if(userName != null) {
            log.info("User disconnected: {}", userName);
            User user = User.builder().name(userName).build();
            var pixelDrawing = PixelDrawing.builder()
                    .messageType(EMessageType.QUITTER.toString())
                    .sender(user)
                    .build();

            messageSendingOperations.convertAndSend("/pixel/public", pixelDrawing);
        }
    }
}
