package com.pepe.miniapp.services;

import com.pepe.miniapp.exceptions.DefaultException;
import com.pepe.miniapp.models.Role;
import com.pepe.miniapp.models.User;
import com.pepe.miniapp.models.enums.ERole;
import com.pepe.miniapp.repositories.RoleRepository;
import com.pepe.miniapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> createRole(ERole roleName) {
        Role role = new Role(roleName);
        roleRepository.save(role);
        return ResponseEntity.ok().body(role);
    }

    public ResponseEntity<?> addRoleByUser(String userId, ERole roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException("User not found"));
        Role role = roleRepository.findByName(roleName).orElseThrow(() -> new DefaultException("Role not found"));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
            return ResponseEntity.ok().body("Role was successfully set for user");
        } else {
            throw new DefaultException("Role already was set for user");
        }
    }
}
