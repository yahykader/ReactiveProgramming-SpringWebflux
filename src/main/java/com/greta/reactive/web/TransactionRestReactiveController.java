package com.greta.reactive.web;

import com.greta.reactive.dao.SocieteRepository;
import com.greta.reactive.dao.TransactionRepository;
import com.greta.reactive.entities.Societe;
import com.greta.reactive.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
public class TransactionRestReactiveController {



    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping(value = "/transactions")
    public Flux<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    @GetMapping(value="/transaction/{id}")
    public Mono<Transaction> getOneTransaction(@PathVariable String id){
        return transactionRepository.findById(id);
    }

    @PostMapping(value="/saveTransaction")
    public Mono<Transaction> saveTransaction(@RequestBody Transaction transaction){
        return  transactionRepository.save(transaction);
    }

    @DeleteMapping(value="/deleteTransaction/{id}")
    public Mono<Void> deleteTransaction(@PathVariable String id){
        return transactionRepository.deleteById(id);
    }

    @PutMapping(value="/putTransaction/{id}")
    public Mono<Transaction> putTransaction(@RequestBody Transaction transaction,@PathVariable String id){
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @GetMapping(value="/findTransactionBySociete/{id}")
    public Flux<Transaction> findTransactionBySociete(@PathVariable  String id){
        Societe societe=new Societe();
        societe.setId(id);
        return transactionRepository.findBySociete(societe);
    }

    @GetMapping(value="/streamTransactions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamTransactions(){
        return transactionRepository.findAll();
    }

    @GetMapping(value="/stream/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> stream(@PathVariable String id){

       return societeRepository.findById(id).flatMapMany(soc -> {

            Flux<Long> interval=Flux.interval(Duration.ofMillis(1000));
            Flux<Transaction> transactionFlux=Flux.fromStream(Stream.generate(()->{
                Transaction transaction=new Transaction();
                transaction.setInstant(Instant.now());
                transaction.setSociete(soc);
                transaction.setPrice(soc.getPrice()*(1+(Math.random()*12-6)/100));

                return  transaction;

            }));
            return Flux.zip(interval,transactionFlux)
                        .map(data->{
                            return  data.getT2();
                        }).share();

        });

    }

    @GetMapping(value="/streamService/{id}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Transaction> streamJson(@PathVariable String id){

        return societeRepository.findById(id).flatMapMany(soc -> {

            Flux<Long> interval=Flux.interval(Duration.ofMillis(1000));
            Flux<Transaction> transactionFlux=Flux.fromStream(Stream.generate(()->{
                Transaction transaction=new Transaction();
                transaction.setInstant(Instant.now());
                transaction.setSociete(soc);
                transaction.setPrice(soc.getPrice()*(1+(Math.random()*12-6)/100));

                return  transaction;

            }));
            return Flux.zip(interval,transactionFlux)
                    .map(data->{
                        return  data.getT2();
                    });

        });

    }


    @GetMapping(value="/events/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Double> streamService(@PathVariable String id){

        WebClient webClient=WebClient.create("http://localhost:8090");

        Flux<Double>eventFlux=webClient.get()
                                      .uri("/streamEvents/"+id)
                                      .retrieve()
                                      .bodyToFlux(Event.class)
                                       .map(data->{
                                           return data.getValue();
                                       });
               return  eventFlux;

    }

    @GetMapping(value="/test")
    public String test(){
        return Thread.currentThread().getName();
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class  Event{
    private Instant instant;
    private double value;
    private String societeID;
}
