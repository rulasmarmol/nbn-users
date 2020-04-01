package nbn.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nbn.entities.User;
import nbn.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
    private UserRepository repository;
    
	@GetMapping("/user")
	public User user(@RequestParam(value = "id", defaultValue="1") String id) {
        Optional<User> user = repository.findById(id);
        if(user.isPresent()){
            return repository.findById(id).get();
        }else{
            return new User();
        }
	}
}