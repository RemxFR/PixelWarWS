package com.pixelWar.serverWs.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WsResponse {

    private PixelDrawing pixelDrawing;
    private String sender;

}
