package com.pepe.miniapp.controllers;

import com.pepe.miniapp.models.enums.ERole;
import com.pepe.miniapp.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/create-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createRole(@RequestParam ERole roleName) {
        return adminService.createRole(roleName);
    }

    @GetMapping("/add-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleByUser(@RequestParam String userId, @RequestParam ERole roleName) {
        return adminService.addRoleByUser(userId, roleName);
    }
}
