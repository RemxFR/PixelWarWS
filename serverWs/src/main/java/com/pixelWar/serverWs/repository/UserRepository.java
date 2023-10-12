package com.pixelWar.serverWs.repository;

import com.pixelWar.serverWs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
Interface pour connecter l'entité User à la BDD, génère les opérations CRUD de base.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    Méthode pour trouver les utilisateurs par nom.
     */
    @Query("SELECT u FROM User u WHERE u.name = ?1")
    Optional<User> findUserByName(String name);
}
