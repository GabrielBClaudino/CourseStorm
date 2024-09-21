package com.unifacisa.coursestorm.Models.User;

public enum UserRole {
    SUPERADMIN("superadmin"),
    ADMIN("admin"),
    PROFESSOR("professor");

    private String role;

    UserRole(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
}
