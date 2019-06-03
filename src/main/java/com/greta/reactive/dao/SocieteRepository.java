package com.greta.reactive.dao;

import com.greta.reactive.entities.Societe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SocieteRepository extends ReactiveMongoRepository<Societe,String> {
}
