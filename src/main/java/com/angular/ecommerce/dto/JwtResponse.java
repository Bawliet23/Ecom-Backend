package com.angular.ecommerce.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private UserDTO client;
    public JwtResponse(String jwttoken,UserDTO client) {
        this.jwttoken = "Bearer "+jwttoken;
        this.client=client;
    }
    public String getToken() {
        return this.jwttoken;
    }

    public UserDTO getClient() {
        return client;
    }
}
