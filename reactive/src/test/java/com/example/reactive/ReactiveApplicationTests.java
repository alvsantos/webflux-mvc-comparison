package com.example.reactive;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.Arrays;

@SpringBootTest
class ReactiveApplicationTests {

	Logger logger = LoggerFactory.getLogger(ReactiveApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	public void exampleMono() {
		Mono<String> mono1 = Mono.just("Fulano");
		Mono<String> mono2 = Mono.empty();
		Mono<Void> mono3 = Mono.just("Teste").then();

		mono1.subscribe();
		mono2.subscribe();
		mono3.subscribe();
	}

	@Test
	public void exampleFlux() {
		Flux<String> flux1 = Flux.just("A", "B", "C");
		Flux<String> flux2 = Flux.fromArray(new String[]{"A", "B", "C"});
		Flux<String> flux3 = Flux.fromIterable(Arrays.asList("A", "B", "C"));

		flux1.subscribe();
		flux2.subscribe();
		flux3.subscribe();
	}

	@Test
	public void exampleDoOnnext() {
		Flux<Integer> flux = Flux.just(1, 2, 3)
				.doOnNext(number -> logger.info("The number is: {}", number));

		flux.subscribe();
	}

	@Test
	public void exampleMap() {
		Flux<Integer> flux = Flux.just(1, 2, 3)
			.map(number -> number + 1)
			.doOnNext(number -> logger.info("The number is: {}", number));

		flux.subscribe();
	}

	@Test
	public void exampleMerge() {
		Flux<String> flux1 = Flux.concat(delayWord("A"), delayWord("B"), delayWord("C"));
		Flux<String> flux2 = Flux.concat(delayWord("D"), delayWord("E"), delayWord("F"));

		Flux<String> merge = Flux.merge(flux1, flux2)
				.doOnNext(word -> logger.info("{}, ", word));

		merge.blockLast();
	}

	@Test
	public void exampleZip() {
		Flux<String> flux1 = Flux.concat(delayWord("A"), delayWord("B"), delayWord("C"));
		Flux<String> flux2 = Flux.concat(delayWord("D"), delayWord("E"), delayWord("F"), delayWord("G"));

		Flux<Tuple2<String, String>> zip = Flux.zip(flux1, flux2)
				.doOnNext(tuple -> logger.info("{}, {}", tuple.getT1(), tuple.getT2()));

		zip.blockLast();
	}

	@Test
	public void exampleConcat() {
		Flux<String> flux1 = Flux.concat(delayWord("A"), delayWord("B"), delayWord("C"));
		Flux<String> flux2 = Flux.concat(delayWord("D"), delayWord("E"), delayWord("F"), delayWord("G"));

		Flux<String> zip = Flux.concat(flux1, flux2)
				.doOnNext(string -> logger.info("{}", string));

		zip.blockLast();
	}

	private Mono<String> delayWord(String word) {
		return Mono.delay(Duration.ofMillis(1000)).thenReturn(word);
	}

	@Test
	public void runOnExample() {
		Flux<Integer> flux1 = Flux.range(1, 10000);

		ParallelFlux<Integer> source = flux1
				.parallel(2)
				.runOn(Schedulers.parallel())
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()));
		source.subscribe();

	}

	@Test
	public void publishOnExample() {
		Flux<Integer> flux1 = Flux.range(1, 1);

		Flux<Integer> source = flux1
				.publishOn(Schedulers.newParallel("PARALLEL-A", 2))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.publishOn(Schedulers.newParallel("PARALLEL-B", 2))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()));
		source.subscribe();

	}

	@Test
	public void subscribeOnExample() {
		Flux<Integer> flux1 = Flux.range(1, 1);

		Flux<Integer> source = flux1
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.publishOn(Schedulers.newParallel("PARALLEL-A", 2))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.publishOn(Schedulers.newParallel("PARALLEL-B", 2))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.doOnNext(value -> logger.info("{} - thread - {}", value, Thread.currentThread().getName()))
				.subscribeOn(Schedulers.newParallel("PARALLEL-C", 2));
		source.subscribe();

	}


	@Test
	public void blockExample() {
		Mono<Integer> mono1 = Mono.just(1);

		Integer result = mono1.block();
		logger.info("{} - thread - {}", result, Thread.currentThread().getName());
	}

}
