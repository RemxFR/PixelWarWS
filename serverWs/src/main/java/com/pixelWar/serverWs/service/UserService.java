package com.pixelWar.serverWs.service;

import com.pixelWar.serverWs.entity.User;
import com.pixelWar.serverWs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
               userSaved = this.userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       return userSaved;
    }
}
