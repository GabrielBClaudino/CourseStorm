package com.unifacisa.coursestorm.Services.User;

import com.unifacisa.coursestorm.Models.User.*;
import com.unifacisa.coursestorm.Repositories.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para verificar se um usuário existe por email
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Método para registrar um novo usuário a partir do RegisterDTO
    public UserDTO registerNewUser(RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(
                data.email(),
                encryptedPassword,
                data.role(),
                data.registrationNumber(),
                data.firstName(),
                data.lastName()
        );
        User savedUser = userRepository.save(newUser);
        return convertToUserDTO(savedUser);
    }

    // Método para listar todos os usuários e retornar como DTO
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != UserRole.SUPERADMIN) // Filtra usuários que não são SUPERADMIN
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    // Método para buscar um usuário por ID e retornar como DTO
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getRole() != UserRole.SUPERADMIN) // Ignora usuários SUPERADMIN
                .map(this::convertToUserDTO);
    }

    // Método para atualizar um usuário existente a partir do UserUpdateDTO
    public Optional<UserDTO> updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setRegistrationNumber(userUpdateDTO.getRegistrationNumber());
            user.setFirstName(userUpdateDTO.getFirstName());
            user.setLastName(userUpdateDTO.getLastName());
            User updatedUser = userRepository.save(user);
            return Optional.of(convertToUserDTO(updatedUser));
        } else {
            return Optional.empty();
        }
    }

    // Método para deletar um usuário por ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Converte um User para UserDTO
    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRegistrationNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
