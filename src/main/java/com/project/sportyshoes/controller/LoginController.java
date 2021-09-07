package com.project.sportyshoes.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.sportyshoes.global.GlobalData;
import com.project.sportyshoes.model.Role;
import com.project.sportyshoes.model.User;
import com.project.sportyshoes.repository.CustomerServices;
import com.project.sportyshoes.repository.RoleRepository;
import com.project.sportyshoes.repository.UserRepository;

import net.bytebuddy.utility.RandomString;

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
	public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request) throws ServletException {

		String passoword = user.getPassword();

		user.setPassword(bCryptPasswordEncoder.encode(passoword));
		List<Role> roles = new ArrayList<Role>();
		roles.add(roleRepository.findById(2).get());
		user.setRoles(roles);
		userRepository.save(user);
		request.login(user.getEmail(), passoword);
		return "redirect:/";

	}
	

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private CustomerServices customerService;
	
//	@Autowired
	private Snippet snippet;
	

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm() {
		System.out.println("Reset PW page loaded");
		return "forgot_password_form";	
		}

	@PostMapping("/forgot_password")	
	public String processForgotPassword(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);
		

		try {
			customerService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			System.out.println(resetPasswordLink);
			Snippet snippet = new Snippet(email, resetPasswordLink);
			snippet.sendEmail();
			//sendEmail(email, resetPasswordLink);
			//snippet.sendEmail(email, resetPasswordLink);
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (Exception ex) {
			model.addAttribute("error", ex.getMessage());
		}
		System.out.println("Reset PW form data posted");
		return "forgot_password_form";
	}

	public void sendEmail(String email, String resetPasswordLink) {

	}

	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		User user = customerService.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		}

		return "reset_password_form";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		User user = customerService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		} else {
			customerService.updatePassword(user, password);

			model.addAttribute("message", "You have successfully changed your password.");
		}

		return "login";
	}

}
