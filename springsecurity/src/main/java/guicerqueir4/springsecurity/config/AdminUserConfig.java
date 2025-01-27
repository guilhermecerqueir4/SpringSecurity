package guicerqueir4.springsecurity.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import guicerqueir4.springsecurity.entities.Role;
import guicerqueir4.springsecurity.entities.User;
import guicerqueir4.springsecurity.repository.RoleRepository;
import guicerqueir4.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner{

	private RoleRepository roleRepository;
	private UserRepository userRepository;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}	
	
	public AdminUserConfig(RoleRepository roleRepository
			, UserRepository userRepository
			) {
		
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}


	@Override
	@Transactional
	public void run(String... args) throws Exception {
		
		var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
		var userAdmin = userRepository.findByUserName("admin");
		
		userAdmin.ifPresentOrElse(
				user -> {
					System.out.println("admin ja existe");
				}, 
				 () -> {
					 var user = new User();
					 user.setUserName("admin");
					 user.setPassword(passwordEncoder().encode("123"));
					 user.setRoles(Set.of(roleAdmin));
					 userRepository.save(user);
					 
				 }
				  
				);

		
	}

}
