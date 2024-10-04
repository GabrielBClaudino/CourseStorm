package com.unifacisa.coursestorm.Models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String email;
    private String password;
    private String registrationNumber;
    private String firstName;
    private String lastName;
    private UserRole role;
}
