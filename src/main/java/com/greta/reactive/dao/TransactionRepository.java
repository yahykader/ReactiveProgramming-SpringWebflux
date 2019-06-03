package com.greta.reactive.dao;

import com.greta.reactive.entities.Societe;
import com.greta.reactive.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {
    public Flux<Transaction> findBySociete(Societe societe);
}
