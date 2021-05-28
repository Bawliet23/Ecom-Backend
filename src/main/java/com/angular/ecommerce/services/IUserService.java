package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.entities.User;

public interface IUserService {
    User addUser(RegisterDTO user);
}
