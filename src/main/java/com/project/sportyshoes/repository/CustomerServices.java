package com.project.sportyshoes.repository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.sportyshoes.model.User;

@Service
@Transactional
public class CustomerServices {
	
	@Autowired
    private CustomerRepository customerRepo;
     
 
    public void updateResetPasswordToken(String token, String email) throws Exception {
        User user = customerRepo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            customerRepo.save(user);
            System.out.println("Found user with the specified email ID");
        } else {
            throw new Exception("Could not find any user with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return customerRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        customerRepo.save(user);
        System.out.println("Updated user PW");
    }

}
