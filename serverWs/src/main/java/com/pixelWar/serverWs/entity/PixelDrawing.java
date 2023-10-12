package com.pixelWar.serverWs.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_canvas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PixelDrawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coordoX")
    private int coordoX;

    @Column(name = "coordoY")
    private int coordoY;

    @Column(name = "color")
    private String color;

    private String sender;

    private String messageType;


}
