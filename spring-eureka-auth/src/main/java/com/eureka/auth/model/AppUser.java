package com.eureka.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUser {

    private Integer id;
    private String username;
    private String password;
    private String role;
}
