package com.unifacisa.coursestorm.Models.User;

import com.unifacisa.coursestorm.Models.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String registrationNumber;
    private String firstName;
    private String lastName;
    private UserRole role;
}
