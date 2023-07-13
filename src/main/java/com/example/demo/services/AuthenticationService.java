package com.example.demo.services;

import com.example.demo.exceptions.EntityExistsException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.jpaRepositories.UserRepository;
import com.example.demo.models.*;
import com.example.demo.requests.AuthenticationRequest;
import com.example.demo.requests.RegisterRequest;
import com.example.demo.dto.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        //String name, String email, String password
        var user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.USER);
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EntityExistsException("User with this email already exists!");
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse register(User user) {
        //String name, String email, String password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EntityExistsException("User with this email already exists!");
        }
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty())
            throw new UsernameNotFoundException("Email not found.");
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new UsernameNotFoundException("Invalid password.");

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        }catch(BadCredentialsException e){
            //This doesnt work correctly, cant find a way to make this
            throw new UsernameNotFoundException("Invalid email or password");
        }


        user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
