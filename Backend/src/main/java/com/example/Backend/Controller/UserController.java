package com.example.Backend.Controller;

import com.example.Backend.Service.UserService;
import com.example.Backend.Service.UserServiceImpl;
import com.example.Backend.dto.RequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<RequestResponse> register(@RequestBody RequestResponse reg) {
        return ResponseEntity.ok(userService.register(reg));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<RequestResponse> login(@RequestBody RequestResponse req){
        return ResponseEntity.ok(userService.login(req));
    }
}
