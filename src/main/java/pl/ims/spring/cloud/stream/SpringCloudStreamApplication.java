package pl.ims.spring.cloud.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


@SpringBootApplication
@Slf4j
public class SpringCloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PollableBean(splittable = true)
    public Supplier<Flux<Country>> pullCountries(RestTemplate restTemplate) {
        return () -> {
            try {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.covid19api.com/countries");
                Country[] array_ = restTemplate.getForObject(builder.toUriString(), Country[].class);

                if (array_ != null && array_.length > 0) {
                    return Flux.just(array_);
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Flux.empty();
            }
        };
    }

    @Bean
    public Function<Flux<Country>, Flux<Result>> pullResults(RestTemplate restTemplate) {
        return value ->
                value
                        .filter(country -> country.getCountry().equals("Poland") || country.getCountry().equals("Germany"))
                        .map(country -> getResults(restTemplate, country))
                        .flatMapIterable(results -> results);
    }

    List<Result> getResults(RestTemplate restTemplate, Country country) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.covid19api.com/total/country/" + country.getCountry());
            Result[] array_ = restTemplate.getForObject(builder.toUriString(), Result[].class);

            if (array_ != null && array_.length > 0) {
                return Arrays.asList(array_);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Bean
    public Consumer<Flux<Result>> printResults() {
        return value -> value.subscribe(System.out::println);
    }

    @Bean
    public Function<Flux<Result>, Flux<Result>> filterDecemberResults() {
        return flux ->
                flux.filter(result -> result.getDate().after(Date.valueOf(LocalDate.of(2020, Month.DECEMBER, 1))));
    }

    @Bean
    public Function<Flux<Result>, Tuple2<Flux<Result>, Flux<Result>>> scatterResults() {
        return flux -> {
            Flux<Result> connectedFlux = flux.publish().autoConnect(2);
            Flux<Result> polandFlux = connectedFlux
                    .filter(result -> result.getCountry().equals("Poland"));
            Flux<Result> germanyFlux = connectedFlux
                    .filter(result -> result.getCountry().equals("Germany"));

            return Tuples.of(polandFlux, germanyFlux);
        };
    }

    @Bean
    public Consumer<Flux<Result>> printPolandResults() {
        return value -> value.subscribe(System.out::println);
    }

    @Bean
    public Consumer<Flux<Result>> printGermanyResults() {
        return value -> value.subscribe(System.out::println);
    }

    @Bean
    public Function<Tuple2<Flux<Result>, Flux<Result>>, Flux<Result>> mergeResults() {
        return tuple -> {
            Flux<Result> stream1 = tuple.getT1();
            Flux<Result> stream2 = tuple.getT2();
            return Flux.merge(stream1, stream2);
        };
    }

    @Bean
    public Consumer<Flux<Result>> printMergedResults() {
        return value -> value.subscribe(System.out::println);
    }
}
