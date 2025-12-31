package com.example.Backend.Controller;

import com.example.Backend.Service.TokenBlacklistService;
import com.example.Backend.Service.UserService;
import com.example.Backend.Service.UserServiceImpl;
import com.example.Backend.dto.RequestResponse;
import com.example.Backend.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private final UserServiceImpl userService;
    private final TokenBlacklistService tokenBlacklistService;

    public UserController(UserServiceImpl userService, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<RequestResponse> register(@RequestBody RequestResponse reg) {
        return ResponseEntity.ok(userService.register(reg));
    }
    @PostMapping("/auth/login")
    public ResponseEntity<RequestResponse> login(@RequestBody RequestResponse req){
        return ResponseEntity.ok(userService.login(req));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> disconnect(
            @RequestHeader("Authorization") String authorizationHeader, Principal principal) {

        String token = authorizationHeader.replace("Bearer ", "");
        tokenBlacklistService.blacklist(token);

        String userEmail = String.valueOf(principal.getName());
        userService.deleteUserFcmToken(userEmail);


        return ResponseEntity.ok("Disconnected successfully");
    }



}
