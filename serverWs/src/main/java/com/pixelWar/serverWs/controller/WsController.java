package com.pixelWar.serverWs.controller;

import com.pixelWar.serverWs.entity.EMessageType;
import com.pixelWar.serverWs.entity.PixelDrawing;
import com.pixelWar.serverWs.service.CanvasService;
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

    public WsController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    @MessageMapping("/pixelWar.addUser")
    @SendTo("/pixel/public")
    public PixelDrawing addUser(@Payload PixelDrawing pixelDrawing,
                                SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", pixelDrawing.getSender());
        return pixelDrawing;
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
