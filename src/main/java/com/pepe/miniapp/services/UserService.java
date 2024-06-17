package com.pepe.miniapp.services;

import com.pepe.miniapp.Bot;
import com.pepe.miniapp.exceptions.DefaultException;
import com.pepe.miniapp.models.Reffs;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.payload.response.UserResponse;
import com.pepe.miniapp.pojo.ReffPJ;
import com.pepe.miniapp.repositories.ReffsRepository;
import com.pepe.miniapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReffsRepository reffsRepository;
    private final Bot bot;

    public UserService(UserRepository userRepository, ReffsRepository reffsRepository, Bot bot) {
        this.userRepository = userRepository;
        this.reffsRepository = reffsRepository;
        this.bot = bot;
    }

    public ResponseEntity<?> getUser(Long telegramId) {
        User user = userRepository.findByTelegramId(telegramId).orElseThrow(() -> new DefaultException("User not found"));
        Reffs reffsList = reffsRepository.findByUser(user).orElse(new Reffs(user));
        List<ReffPJ> reffs = new ArrayList<>();
        for (User userListed : reffsList.getReffs()) {
            reffs.add(new ReffPJ(userListed.getName(), userListed.getScore()));
        }
        UserResponse response = new UserResponse(user.getScore(), reffs);
        return ResponseEntity.ok().body(response);
    }
}
