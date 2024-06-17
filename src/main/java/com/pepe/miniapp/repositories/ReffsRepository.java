package com.pepe.miniapp.repositories;

import com.pepe.miniapp.models.Reffs;
import com.pepe.miniapp.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReffsRepository extends MongoRepository<Reffs, String> {
    Optional<Reffs> findByUser(User user);
}
