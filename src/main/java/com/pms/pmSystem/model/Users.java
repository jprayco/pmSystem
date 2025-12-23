package com.pms.pmSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String lname, fname;
    private String email;
    private int status;
    private String phone;
    private String role;
    private String username;
    private String password;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
