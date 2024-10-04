package com.unifacisa.coursestorm.Models.User;

public record RegisterDTO(String email, String password, UserRole role, String registrationNumber, String firstName, String lastName) {
}

