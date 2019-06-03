package com.greta.reactive;

import com.greta.reactive.dao.SocieteRepository;
import com.greta.reactive.dao.TransactionRepository;
import com.greta.reactive.entities.Societe;
import com.greta.reactive.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactiveApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveApplication.class, args);
    }
    @Autowired
    private SocieteRepository societeRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public void run(String... args) throws Exception {

        societeRepository.deleteAll().subscribe(null,null,()->{

            Stream.of("BNP Paribas","Sociéte Génerale","Crédit Agrigole","LCL","Banque Postale").forEach(s->{
                societeRepository.save(new Societe(s,s,100+Math.random()*900))
                          .subscribe(soc->{
                               System.out.println(soc.toString());
                        });
                 });

                transactionRepository.deleteAll().subscribe(null,null,()->{
                        Stream.of("BNP Paribas","Sociéte Génerale","Crédit Agrigole","LCL","Banque Postale").forEach(s->{
                            societeRepository.findById(s).subscribe(soc->{

                                for (int i = 0; i <10; i++) {
                                    Transaction transaction=new Transaction();
                                    transaction.setInstant(Instant.now());
                                    transaction.setSociete(soc);
                                    transaction.setPrice(soc.getPrice()*(1+(Math.random()*12-6)/100));
                                    transactionRepository.save(transaction).subscribe(t->{
                                        System.out.println(t.toString());
                                    });
                                }
                            });
                        });

                });

        });
    }
}
