package dev.lorena.fotitoscumple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@EnableAsync
@SpringBootApplication
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FotitoscumpleApplication {

	static void main(String... args) {
		SpringApplication.run(FotitoscumpleApplication.class, args);
	}

}
