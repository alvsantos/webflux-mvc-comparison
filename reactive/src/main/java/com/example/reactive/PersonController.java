package com.example.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class PersonController {

  private final WebClient client;

  public PersonController(WebClient.Builder builder) {
    this.client = builder.baseUrl("http://localhost:3004").build();
  }

  @GetMapping("person")
  public Mono<Person> get() {
    return this.client.get().uri("/person").accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Person.class);
  }

}