package com.greta.reactive.web;

import com.greta.reactive.dao.SocieteRepository;
import com.greta.reactive.entities.Societe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SocieteRestReactiveController {

  @Autowired
  private SocieteRepository societeRepository;

  @GetMapping(value = "/societes")
  public Flux<Societe> findAll(){
      return societeRepository.findAll();
  }

  @GetMapping(value="/societe/{id}")
  public Mono<Societe> getOneSociete(@PathVariable  String id){
      return societeRepository.findById(id);
  }

  @PostMapping(value="/saveSociete")
  public Mono<Societe> saveSociete(@RequestBody Societe societe){
      return  societeRepository.save(societe);
  }

  @DeleteMapping(value="/deleteSociete/{id}")
  public Mono<Void> deleteSociete(@PathVariable String id){
      return societeRepository.deleteById(id);
  }

  @PutMapping(value="/putSociete/{id}")
  public Mono<Societe> putSociete(@RequestBody Societe societe,@PathVariable String id){
      societe.setId(id);
      return societeRepository.save(societe);
  }

}
