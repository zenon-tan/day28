package day28.miniexercise;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import day28.miniexercise.repositories.PlaystoreRepo;

@SpringBootApplication
public class MiniExerciseApplication implements CommandLineRunner {

	@Autowired
	PlaystoreRepo psRepo;


	public static void main(String[] args) {
		SpringApplication.run(MiniExerciseApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		List<Document> results = psRepo.groupAppByCategory();

		for(Document d : results) {
			System.out.println(d.toJson());
			
		}
		
	}

}
