package com.angular.ecommerce.services;

import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.entities.Role;
import com.angular.ecommerce.entities.User;
import com.angular.ecommerce.repositories.IRoleRepository;
import com.angular.ecommerce.repositories.IUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public User addUser(RegisterDTO registerDTO) {

        User user = modelMapper.map(registerDTO,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().clear();
        for (String role : registerDTO.getRoleRole()){
            Role role1 = roleRepository.findByRole(role);
            user.getRoles().add(role1);
        }
        return userRepository.save(user);
    }
}
