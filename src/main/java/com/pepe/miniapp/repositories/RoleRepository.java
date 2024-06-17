package com.pepe.miniapp.repositories;

import com.pepe.miniapp.models.Role;
import com.pepe.miniapp.models.enums.ERole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
