package com.angular.ecommerce.controllers;

import com.angular.ecommerce.dto.JwtRequest;
import com.angular.ecommerce.dto.JwtResponse;
import com.angular.ecommerce.dto.RegisterDTO;
import com.angular.ecommerce.dto.UserDTO;
import com.angular.ecommerce.entities.User;
import com.angular.ecommerce.security.MyUserPrincipal;
import com.angular.ecommerce.services.IUserService;
import com.angular.ecommerce.utils.Mapper;
import com.angular.ecommerce.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        System.out.println(authenticationRequest.getUsername());
        System.out.println(passwordEncoder.encode(authenticationRequest.getPassword()));
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final MyUserPrincipal userDetails = (MyUserPrincipal) userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        UserDTO user = Mapper.convertUserToUserDTO(userDetails.getUser());
        return ResponseEntity.ok(new JwtResponse(token,user));
    }
    @PostMapping(value = "/signUp",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody RegisterDTO user) throws Exception {
        user.getRole().add("CLIENT");
            User client1=userService.addUser(user);
            UserDetails userDetails=new MyUserPrincipal(client1);
            final String token = jwtTokenUtil.generateToken(userDetails);
        UserDTO userdto = Mapper.convertUserToUserDTO(client1);
            return ResponseEntity.ok(new JwtResponse(token,userdto));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
