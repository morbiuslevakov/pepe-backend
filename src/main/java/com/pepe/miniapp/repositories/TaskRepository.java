package com.pepe.miniapp.repositories;

import com.pepe.miniapp.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
