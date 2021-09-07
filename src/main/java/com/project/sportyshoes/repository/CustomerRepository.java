package com.project.sportyshoes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.sportyshoes.model.User;

public interface CustomerRepository extends CrudRepository<User, Integer> {
	
	public User user = null;
	
    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByEmail(String email); 
    public User findByResetPasswordToken(String token);
   
}