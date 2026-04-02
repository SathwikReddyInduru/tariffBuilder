package com.xius.TarrifBuilder.Dao;

import lombok.Data;

@Data
public class LoginForm {

    private String role; // USER or ADMIN
    private String networkName; // Only for USER
    private String username;
    private String password;
}