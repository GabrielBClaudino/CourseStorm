package com.unifacisa.coursestorm;

import com.unifacisa.coursestorm.Models.User.User;
import com.unifacisa.coursestorm.Models.User.UserRole;
import com.unifacisa.coursestorm.Repositories.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CoursestormApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(CoursestormApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Verifica se já existe um usuário SuperAdmin pelo email
		if (!userRepository.existsByEmail("superadmin@admin.com")) { // Alteração para existsByEmail
			// Cria um SuperAdmin padrão
			User superAdmin = new User();
			superAdmin.setEmail("superadmin@admin.com");
			superAdmin.setPassword(passwordEncoder.encode("superadmin123"));
			superAdmin.setRole(UserRole.valueOf("SUPERADMIN"));

			/*
			{
 			 "email": "superadmin@admin.com",
  			 "password": "superadmin123"
			 }
			*/
			// Salva o usuário no repositório
			userRepository.save(superAdmin);

			System.out.println("Usuário SuperAdmin criado com sucesso!");
		} else {
			System.out.println("Usuário SuperAdmin já existe. Nenhuma ação necessária.");
		}
	}
}
