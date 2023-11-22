package com.quipux.playlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class PlaylistBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaylistBackendApplication.class, args);
	}

}
