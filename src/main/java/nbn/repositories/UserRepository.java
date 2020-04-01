package nbn.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import nbn.entities.User;

public interface UserRepository extends MongoRepository<User, String> {

  public User findByName(String name);
  public List<User> findByAge(int age);
}