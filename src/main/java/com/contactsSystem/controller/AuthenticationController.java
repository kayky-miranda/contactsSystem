package com.contactsSystem.controller;

import com.contactsSystem.domain.User;
import com.contactsSystem.dto.AuthenticationDTO;
import com.contactsSystem.dto.LoginResponseDTO;
import com.contactsSystem.dto.RegisterDTO;
import com.contactsSystem.infra.security.security.TokenService;
import com.contactsSystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        User user = (User) userRepository.findByLogin(data.login());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }if (passwordEncoder.matches(data.password(),user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(),token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data){
        User user = (User) userRepository.findByLogin(data.login());

        if(user == null) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(data.password()));
            newUser.setLogin(data.login());
            newUser.setRole(data.role());
            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok(new LoginResponseDTO(newUser.getUsername(),token));
        }
        return ResponseEntity.badRequest().build();
    }
}
