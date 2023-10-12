package com.pixelWar.serverWs.service;

import com.pixelWar.serverWs.entity.User;
import com.pixelWar.serverWs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Classe qui gère l'ajout et la suppression d'utilisateur.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        User userSaved = null;
        try {
            if(user != null) {
                Optional<User> userCheck = this.userRepository.findUserByName(user.getName());
                if(userCheck.isEmpty()) {
                    userSaved = this.userRepository.save(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return userSaved;
    }

    public void deleteUser(String username) {
        Optional<User> userToDeleteOpt = this.userRepository.findUserByName(username);
        User userToDelete = null;

        if(userToDeleteOpt.isPresent()) {
            userToDelete = userToDeleteOpt.get();
            this.userRepository.delete(userToDelete);
        }
    }
}
