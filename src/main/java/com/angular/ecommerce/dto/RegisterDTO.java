package com.angular.ecommerce.dto;

import lombok.Data;

@Data
public class RegisterDTO extends UserDTO{
    private String password;
}
