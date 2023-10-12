package com.pixelWar.serverWs.controller;

import com.pixelWar.serverWs.entity.EMessageType;
import com.pixelWar.serverWs.entity.PixelDrawing;
import com.pixelWar.serverWs.entity.User;
import com.pixelWar.serverWs.entity.WsResponse;
import com.pixelWar.serverWs.service.CanvasService;
import com.pixelWar.serverWs.service.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WsController {


    private final CanvasService canvasService;
    private final UserService userService;

    public WsController(CanvasService canvasService, UserService userService) {

        this.canvasService = canvasService;
        this.userService = userService;
    }

    @MessageMapping("/pixelWar.addUser")
    @SendTo("/pixel/public")
    public WsResponse addUser(@Payload PixelDrawing pixelDrawing,
                              SimpMessageHeaderAccessor headerAccessor) {
        User user = User.builder().name(pixelDrawing.getSender()).build();
        User userToSave = null;
        if(user.getName() != null) {
           userToSave = this.userService.saveUser(user);
        }
        headerAccessor.getSessionAttributes().put("username", pixelDrawing.getSender());
        return WsResponse.builder()
                .pixelDrawing(pixelDrawing)
                .user(userToSave)
                .build();
    }

    @MessageMapping("/pixelWar.drawPixel")
    @SendTo("/pixel/public")
    public PixelDrawing drawPixel(@Payload PixelDrawing pixelDrawing) {
        if (pixelDrawing != null) {
            this.canvasService.savePixels(pixelDrawing);
        }
        return pixelDrawing;
    }

    @MessageMapping("/pixelWar.getPixels")
    @SendTo("/pixel/public")
    public List<PixelDrawing> getPixels(@Payload PixelDrawing pixelDrawing) {
        List<PixelDrawing> pixelDrawingList = new ArrayList<>();
        System.out.println("Affichage du canvas en préparation");
        System.out.println(pixelDrawing.getMessageType());
        System.out.println(EMessageType.AFFICHER_CANVAS);
        if(pixelDrawing.getMessageType().equals(EMessageType.AFFICHER_CANVAS.toString())) {
            pixelDrawingList = this.canvasService.findAllPixels();
            System.out.println("Canvas trouvé en bdd");
        }
        return pixelDrawingList;
    }
}
