package com.example.event.repository;

import com.example.event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event,String> {
    Optional<Event> readByName(String name);
    boolean existsByName(String name);
}
