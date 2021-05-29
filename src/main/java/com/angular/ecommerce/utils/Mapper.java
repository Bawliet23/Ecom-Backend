package com.angular.ecommerce.utils;

import com.angular.ecommerce.dto.UserDTO;
import com.angular.ecommerce.entities.Role;
import com.angular.ecommerce.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class Mapper {


     private  static ModelMapper modelmapper = new ModelMapper();
    public static UserDTO convertUserToUserDTO(User user){
        UserDTO userDTO = modelmapper.map(user,UserDTO.class);
        for(Role role : user.getRoles()){
            userDTO.getRole().add(role.getRole());
        }
        return userDTO;
    }
}
