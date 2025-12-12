package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class RegisterController {
	private UserService userService;
	private PasswordEncoder passwordEncoder;

	public RegisterController(UserService userService,PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/auth/register")
	public ResponseEntity<String> register(@RequestBody User userRequest) throws IdInvalidException {
		boolean isEmailExist = userService.isEmailExist(userRequest.getEmail());
		if (isEmailExist) {
			throw new IdInvalidException(
					"Email " + userRequest.getEmail() + " da ton tai, vui long su dung email khac");
		}
		userRequest.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
		userService.handleCreateUser(userRequest);
		return ResponseEntity.ok("Register success");
	}
}