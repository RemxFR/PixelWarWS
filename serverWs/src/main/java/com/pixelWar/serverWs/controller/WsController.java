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

/*
Contrôleur ws pour gérer la connexion à la websocket, l'enregistrement et la modification des pixels, l'enregistrement
 et la suppression des users et la récupérationd e tous les pixels enregistrés.
 */
@Controller
public class WsController {


    private final CanvasService canvasService;
    private final UserService userService;

    public WsController(CanvasService canvasService, UserService userService) {
        this.canvasService = canvasService;
        this.userService = userService;
    }

    //Pour persister un utilisateur
    @MessageMapping("/pixelWar.addUser")
    @SendTo("/pixel/public")
    public WsResponse addUser(@Payload PixelDrawing pixelDrawing,
                              SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("Test persitence user");
        headerAccessor.getSessionAttributes().put("username", pixelDrawing.getSender());
        return WsResponse.builder()
                .pixelDrawing(pixelDrawing)
                .sender(pixelDrawing.getSender())
                .build();
    }

    //Pour supprimer un utilisateur
    @MessageMapping("/pixelWar.deconnectUser")
    @SendTo("/pixel/public")
    public PixelDrawing deconnectUser(@Payload PixelDrawing pixelDrawing) {
        return pixelDrawing;
    }

    //Pour Ajouter, modifier un pixel.
    @MessageMapping("/pixelWar.drawPixel")
    @SendTo("/pixel/public")
    public PixelDrawing drawPixel(@Payload PixelDrawing pixelDrawing) {
        if (pixelDrawing != null) {
            if(pixelDrawing.getId() != null) {
                this.canvasService.updatePixel(pixelDrawing);
            }
            this.canvasService.savePixels(pixelDrawing);
        }
        return pixelDrawing;
    }

    //POur récupérer tous les pixels dessinés.
    @MessageMapping("/pixelWar.getPixels")
    @SendTo("/pixel/public")
    public List<PixelDrawing> getPixels(@Payload PixelDrawing pixelDrawing) {
        List<PixelDrawing> pixelDrawingList = new ArrayList<>();
        if (pixelDrawing.getMessageType().equals(EMessageType.AFFICHER_CANVAS.toString())) {
            pixelDrawingList = this.canvasService.findAllPixels();
        }
        return pixelDrawingList;
    }
}
