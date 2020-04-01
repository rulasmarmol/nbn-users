package nbn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nbn.entities.User;
import nbn.repositories.UserRepository;

@SpringBootApplication
@RestController
@EnableScheduling
public class Application implements CommandLineRunner {

	@Autowired
	private UserRepository repository;
  
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();
	
		// save a couple of customers
		repository.save(new User("Carlos", 32));
		repository.save(new User("Citlalli", 29));
	
		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (User user : repository.findAll()) {
		  System.out.println(user);
		}
		System.out.println();
	
		// fetch an individual customer
		System.out.println("User found with findByName('Carlos'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByName("Carlos"));
	
		System.out.println("Customers found with findByAge(29):");
		System.out.println("--------------------------------");
		for (User user : repository.findByAge(29)) {
		  System.out.println(user);
		}
	}

}
