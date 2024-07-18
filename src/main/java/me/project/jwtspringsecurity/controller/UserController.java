package me.project.jwtspringsecurity.controller;

import me.project.jwtspringsecurity.entity.AuthRequest;
import me.project.jwtspringsecurity.entity.UserInfo;
import me.project.jwtspringsecurity.service.CustomUserDetailsService;
import me.project.jwtspringsecurity.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secured";
    }

    @PostMapping("add-new-user")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return userService.addUser(userInfo);
    }

    @GetMapping("users/user-profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("admin/admin-profile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("generate-token")
    public String generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid User Request "+ authRequest.getUsername());
        }
    }
}
