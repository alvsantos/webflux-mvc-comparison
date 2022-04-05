package com.example.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PersonController {

  private final WebClient client;
  private final PersonRepository repository;

  public PersonController(WebClient.Builder builder, PersonRepository personRepository) {
    this.client = builder.baseUrl("http://localhost:3004").build();
    this.repository = personRepository;
  }

  @GetMapping("person")
  public Mono<Person> get(@RequestParam("name") String name) {
//    return this.client.get().uri("/person").accept(MediaType.APPLICATION_JSON)
//            .retrieve()
//            .bodyToMono(Person.class);

    return this.repository.findByName(name);
  }

  public Mono<Person> exampleFlatMap(String name) {
    return Mono.just(name)
            .flatMap(this.repository::findByName);
  }

  public Flux<Person> exampleFlatMapMany(String age) {
    return Mono.just(age)
            .flatMapMany(this.repository::findByAge);
  }

  public Mono<List<Person>> exampleCollectList(String age) {
    return Mono.just(age)
            .flatMapMany(this.repository::findByAge)
            .collectList();
  }

}