package com.aluracursos.screenmatch;


import com.aluracursos.screenmatch.principal.Principal2;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class ScreenmatchApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

}
