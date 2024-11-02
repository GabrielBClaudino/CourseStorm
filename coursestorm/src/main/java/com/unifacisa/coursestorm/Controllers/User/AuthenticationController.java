package com.unifacisa.coursestorm.Controllers.User;

import com.unifacisa.coursestorm.Infra.Security.User.TokenService;
import com.unifacisa.coursestorm.Models.User.*;
import com.unifacisa.coursestorm.Services.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Autentica o usuário",
            description = "Realiza a autenticação do usuário com base nas credenciais fornecidas e retorna um token de autenticação JWT.",
            method = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, retorna o token JWT."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para autenticação."),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(
            summary = "Registra um novo usuário",
            description = "Registra um novo usuário no sistema com as informações fornecidas, verificando se o e-mail já está em uso.",
            method = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "E-mail já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (userService.emailExists(data.email())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        userService.registerNewUser(data);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(
            summary = "Obtém todos os usuários",
            description = "Retorna uma lista de todos os usuários registrados no sistema, excluindo usuários com a role SUPERADMIN.",
            method = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Obtém um usuário pelo ID",
            description = "Busca um usuário específico no sistema com base no ID fornecido, ignorando usuários com a role SUPERADMIN.",
            method = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualiza as informações de um usuário",
            description = "Atualiza as informações de um usuário específico com base no ID fornecido.",
            method = "PUT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para atualização."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO userUpdateDTO) {
        Optional<UserDTO> updatedUser = userService.updateUser(id, userUpdateDTO);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deleta um usuário pelo ID",
            description = "Remove um usuário específico do sistema com base no ID fornecido.",
            method = "DELETE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação.")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
