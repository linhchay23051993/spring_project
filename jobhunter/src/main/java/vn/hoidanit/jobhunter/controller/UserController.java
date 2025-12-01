package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDto;
import vn.hoidanit.jobhunter.domain.response.ResUserDto;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDto;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;
	private PasswordEncoder passwordEncoder;

	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/users")
	@ApiMessage("Create new user")
	public ResponseEntity<ResCreateUserDto> createUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
		boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
		if (isEmailExist) {
			throw new IdInvalidException(
					"Email " + postManUser.getEmail() + " da ton tai, vui long su dung email khac");
		}

		String hashPassword = passwordEncoder.encode(postManUser.getPassword());
		postManUser.setPassword(hashPassword);
		User user = this.userService.handleCreateUser(postManUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDto(user));
	}

	@DeleteMapping("/users/{id}")
	@ApiMessage("Delete user by id")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
		User currentUser = this.userService.fetchUserById(id);

		if (currentUser == null) {
			throw new IdInvalidException("User voi id = " + id + " khong ton tai");
		}
		this.userService.handleDeleteUser(id);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/users/{id}")
	@ApiMessage("Fetch all  by id")
	public ResponseEntity<ResUserDto> getUserById(@PathVariable("id") long id) throws IdInvalidException {
		User currentUser = this.userService.fetchUserById(id);
		if (currentUser == null) {
			throw new IdInvalidException("User voi id = " + id + " khong ton tai");
		}
		User fetchUser = this.userService.fetchUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDto(fetchUser));
	}

	@GetMapping("/users")
	@ApiMessage("Fetch all user")
	public ResponseEntity<ResultPaginationDTO> getAllUser(
			@Filter Specification<User> spec,
			Pageable pageable) {

		return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAllUser(spec, pageable));
	}

	@PutMapping("/users")
	@ApiMessage("Update user")
	public ResponseEntity<ResUpdateUserDto> updateUser(@RequestBody User user) throws IdInvalidException {
		User currentUser = this.userService.handleUpdateUSer(user);
		if (currentUser == null) {
			throw new IdInvalidException("User voi id = " + user.getId() + " khong ton tai");
		}
		return ResponseEntity.ok(this.userService.convertToResUpdateUserDto(currentUser));
	}
}
