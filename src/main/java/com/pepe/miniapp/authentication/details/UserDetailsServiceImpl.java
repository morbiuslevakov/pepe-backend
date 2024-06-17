package com.pepe.miniapp.authentication.details;

import com.pepe.miniapp.exceptions.DefaultException;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDetailsImpl loadUserByTelegramId(Long telegramId) throws UsernameNotFoundException {
        User user;
        if (!userRepository.existsByTelegramId(telegramId)) {
            user = new User(telegramId);
            userRepository.save(user);
        } else {
            user = userRepository.findByTelegramId(telegramId).orElseThrow(() -> new DefaultException("Internal server error"));
        }
        return UserDetailsImpl.build(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}