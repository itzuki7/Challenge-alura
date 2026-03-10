package com.alura.demo1;

import com.alura.demo1.principal.Principal;
import com.alura.demo1.repository.AutorRepository;
import com.alura.demo1.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepo;
	@Autowired
	private AutorRepository autorRepo;

	public static void main(String[] args) {
		SpringApplication.run(ChallengApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Principal principal = new Principal(libroRepo, autorRepo);
		principal.muestraElMenu();
	}
}