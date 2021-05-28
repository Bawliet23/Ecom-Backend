package com.angular.ecommerce.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String tel;
    private List<String> roleRole = new ArrayList<>();
}
