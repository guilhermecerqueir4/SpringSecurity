package guicerqueir4.springsecurity.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import guicerqueir4.springsecurity.dto.CreateUserDto;
import guicerqueir4.springsecurity.entities.Role;
import guicerqueir4.springsecurity.entities.User;
import guicerqueir4.springsecurity.repository.RoleRepository;
import guicerqueir4.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;

@RestController
public class UserController {

	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	

	public UserController(UserRepository userRepository, RoleRepository roleRepository) {
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	
	@PostMapping("/users")
	@Transactional
	public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
		
		var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
		
		var userFromDb = userRepository.findByUserName(dto.username());
		
		if(userFromDb.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		var user = new User();
		user.setUserName(dto.username());
		user.setPassword(passwordEncoder.encode(dto.password()));
		user.setRoles(Set.of(basicRole));
		userRepository.save(user);
		
		
		return ResponseEntity.ok().build();
	}
	
}
