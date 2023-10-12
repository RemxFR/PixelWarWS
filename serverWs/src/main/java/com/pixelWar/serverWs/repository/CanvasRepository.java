package com.pixelWar.serverWs.repository;

import com.pixelWar.serverWs.entity.PixelDrawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
Interface pour connecter l'entité PixelDrawing à la BDD, génère les opérations CRUD de base.
 */
@Repository
public interface CanvasRepository extends JpaRepository<PixelDrawing,Long> {

    @Query("select p FROM PixelDrawing p")
    Optional<List<PixelDrawing>> getAllPixels();
}
