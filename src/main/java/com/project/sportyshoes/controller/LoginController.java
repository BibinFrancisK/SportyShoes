package com.project.sportyshoes.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.sportyshoes.global.GlobalData;
import com.project.sportyshoes.model.Role;
import com.project.sportyshoes.model.User;
import com.project.sportyshoes.repository.RoleRepository;
import com.project.sportyshoes.repository.UserRepository;

@Controller
public class LoginController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/login")
	public String login() {
		GlobalData.cart.clear();
		return "login";
	}
	
	@GetMapping("/register")
	public String registerGet() {
		return "register";
	}
	
	@PostMapping("/register")
	public String registerPost(@ModelAttribute("user") User user , HttpServletRequest request) throws ServletException {
			
		String passoword = user.getPassword();
		
		user.setPassword(bCryptPasswordEncoder.encode(passoword));
		List<Role>roles = new ArrayList<Role>();
		roles.add(roleRepository.findById(2).get());
		user.setRoles(roles);
		userRepository.save(user);
		request.login(user.getEmail(), passoword);
		return "redirect:/";
		
	}
	
}

