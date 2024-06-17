package com.pepe.miniapp.controllers;

import com.pepe.miniapp.authentication.details.UserDetailsImpl;
import com.pepe.miniapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUser() {
        Long id = Long.valueOf(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        return userService.getUser(id);
    }
}
