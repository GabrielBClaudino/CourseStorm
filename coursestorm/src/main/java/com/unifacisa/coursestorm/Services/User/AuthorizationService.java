package com.unifacisa.coursestorm.Services.User;

import com.unifacisa.coursestorm.Models.User.UserCreateDTO;
import com.unifacisa.coursestorm.Models.User.UserDTO;
import com.unifacisa.coursestorm.Models.User.UserUpdateDTO;
import com.unifacisa.coursestorm.Models.User.User;
import com.unifacisa.coursestorm.Repositories.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Implementação de UserDetailsService para carregar usuário por e-mail
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    // Método para listar todos os usuários e retornar como DTO
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    // Método para buscar um usuário por ID e retornar como DTO
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToUserDTO);
    }

    // Método para criar um novo usuário a partir do UserCreateDTO
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        User newUser = new User(
                userCreateDTO.getEmail(),
                passwordEncoder.encode(userCreateDTO.getPassword()),
                userCreateDTO.getRole(),
                userCreateDTO.getRegistrationNumber(),
                userCreateDTO.getFirstName(),
                userCreateDTO.getLastName()
        );
        User savedUser = userRepository.save(newUser);
        return convertToUserDTO(savedUser);
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

    // Método para verificar se um usuário existe por email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
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
