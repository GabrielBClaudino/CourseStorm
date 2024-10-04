package com.unifacisa.coursestorm.Controllers.User;

import com.unifacisa.coursestorm.Infra.Security.User.TokenService;
import com.unifacisa.coursestorm.Models.User.AuthenticationDTO;
import com.unifacisa.coursestorm.Models.User.LoginResponseDTO;
import com.unifacisa.coursestorm.Models.User.RegisterDTO;
import com.unifacisa.coursestorm.Models.User.User;
import com.unifacisa.coursestorm.Repositories.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        // Verifica se o e-mail já está em uso
        if (this.repository.existsByEmail(data.email())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        // Codifica a senha
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        // Cria um novo usuário com todos os campos necessários
        User newUser = new User(
                data.email(),
                encryptedPassword,
                data.role(),
                data.registrationNumber(),
                data.firstName(),
                data.lastName()
        );

        // Salva o usuário no repositório
        this.repository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
}
