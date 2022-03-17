package com.example.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

}