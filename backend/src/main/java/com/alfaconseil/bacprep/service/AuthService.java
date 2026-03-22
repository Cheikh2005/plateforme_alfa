package com.alfaconseil.bacprep.service;

import com.alfaconseil.bacprep.dto.JwtResponse;
import com.alfaconseil.bacprep.dto.LoginRequest;
import com.alfaconseil.bacprep.dto.RegisterRequest;
import com.alfaconseil.bacprep.model.User;
import com.alfaconseil.bacprep.repository.UserRepository;
import com.alfaconseil.bacprep.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setDerniereConnexion(LocalDateTime.now());
        userRepository.save(user);

        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(),
                user.getNom(), user.getPrenom(), user.getRole().name(), user.getSerie());
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setSerie(request.getSerie() != null ? request.getSerie() : "C");

        return userRepository.save(user);
    }

    public User getProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public User updateProfile(String username, User updates) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (updates.getNom() != null) user.setNom(updates.getNom());
        if (updates.getPrenom() != null) user.setPrenom(updates.getPrenom());
        if (updates.getSerie() != null) user.setSerie(updates.getSerie());
        return userRepository.save(user);
    }
}