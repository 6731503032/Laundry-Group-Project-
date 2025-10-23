package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    @Autowired
    private LoginService loginService;
    
    @GetMapping("/")
    public String showLoginPage() {
        return "login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            HttpSession session,
            Model model) {
        
        User user = loginService.validateUser(name, password, role);
        
        if (user != null) {
            // Store user in session
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());
            
            // Redirect to booking page
            return "redirect:/booking";
        } else {
            // Login failed
            model.addAttribute("error", "Invalid username, password, or role. Please try again.");
            model.addAttribute("name", name);
            model.addAttribute("selectedRole", role);
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/booking")
    public String bookingPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "booking";
    }
}
