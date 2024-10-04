package com.unifacisa.coursestorm.Controllers.User;

import com.unifacisa.coursestorm.Models.User.UserCreateDTO;
import com.unifacisa.coursestorm.Models.User.UserDTO;
import com.unifacisa.coursestorm.Models.User.UserUpdateDTO;
import com.unifacisa.coursestorm.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        System.out.println("Acessando getAllUsers...");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    //buscar um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //criar um novo usuário
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.ok(createdUser);
    }

    //atualizar um usuário
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserUpdateDTO userUpdateDTO) {
        Optional<UserDTO> updatedUser = userService.updateUser(id, userUpdateDTO);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //deletar um usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

