package com.pixelWar.serverWs.service;

import com.pixelWar.serverWs.entity.PixelDrawing;
import com.pixelWar.serverWs.repository.CanvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*
Classe qui gère les requêtes Get, Post et Update pour les pixels du canvas.
 */
@Service
public class CanvasService {

    private final CanvasRepository canvasRepository;

    @Autowired
    public CanvasService(CanvasRepository canvasRepository) {
        this.canvasRepository = canvasRepository;
    }

    public void savePixels(PixelDrawing pixelDrawing){
        if(pixelDrawing != null) {
            this.canvasRepository.save(pixelDrawing);
        }
    }

    public PixelDrawing getPixelById(PixelDrawing pixelDrawing) {
        PixelDrawing drawing = null;
        if(pixelDrawing != null) {
           drawing = this.canvasRepository.getById(pixelDrawing.getId());
        }
        return drawing;
    }

    public PixelDrawing updatePixel(PixelDrawing pixelDrawing) {
        PixelDrawing drawing = null;
        if(pixelDrawing != null) {
            drawing.setCoordoX(pixelDrawing.getCoordoX());
            drawing.setCoordoY(pixelDrawing.getCoordoY());
            drawing.setColor(pixelDrawing.getColor());
            this.canvasRepository.save(drawing);
        }
        return drawing;
    }

    public List<PixelDrawing> findAllPixels() {
        List<PixelDrawing> pixelDrawingList = new ArrayList<>();
        Optional<List<PixelDrawing>> pixelDrawings = this.canvasRepository.getAllPixels();
        if(pixelDrawings.isPresent()) {
            pixelDrawingList = pixelDrawings.get();
        }
        return pixelDrawingList;
    }
}
