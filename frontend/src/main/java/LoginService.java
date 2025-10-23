package com.example.booking.service;

import com.example.booking.model.User;
import com.example.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User validateUser(String name, String password, String role) {
        Optional<User> userOpt = userRepository.findByNameAndPassword(name, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Validate role matches
            if (user.getRole().equalsIgnoreCase(role)) {
                return user;
            }
        }
        return null;
    }
    
    public boolean isValidCredentials(String name, String password, String role) {
        return validateUser(name, password, role) != null;
    }
}