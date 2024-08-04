package com.sarmad.app.repository;

import com.sarmad.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'loginId': ?0 }")
    Optional<User> findByLoginId(String loginId);

    @Query("{ 'firstName': ?0, 'secondName': ?1 }")
    List<User> findByFirstNameAndSecondName(String firstName, String secondName);

    @Query("{ 'firstName': ?0 }")
    List<User> findByFirstName(String firstName);

    @Query("{ 'secondName': ?0 }")
    List<User> findBySecondName(String secondName);
}