package guicerqueir4.springsecurity.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guicerqueir4.springsecurity.dto.LoginRequest;
import guicerqueir4.springsecurity.dto.LoginResponse;
import guicerqueir4.springsecurity.repository.UserRepository;

@RestController
public class TokenController {
	
	private final JwtEncoder jwtEncoder;
	private final UserRepository userRepository;
	
	@Autowired    
	private PasswordEncoder passwordEncoder;


	public TokenController(JwtEncoder jwtEncoder,UserRepository userRepository) { 
		this.jwtEncoder = jwtEncoder;
		this.userRepository = userRepository;;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
		
		var user = userRepository.findByUserName(loginRequest.username());
		
		
		if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
			throw new BadCredentialsException("User or password is invalid"); //User vazio ou login incorreto
		}
		
		var now = Instant.now();
		var expiresIn = 300L; //5 mins
		
		var claims = JwtClaimsSet.builder()
				.subject("myBackEnd") // Identificador do usuário
                .issuer(user.get().getUserId().toString()) // Quem emitiu o token
                .issuedAt(now) //Dt de emissao do token
                .expiresAt(now.plusSeconds(expiresIn)) // Expiração
                .build();
		
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
		
	}

}
