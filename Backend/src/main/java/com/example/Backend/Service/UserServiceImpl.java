package com.example.Backend.Service;

import com.example.Backend.Model.User;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.RequestResponse;
import com.example.Backend.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public RequestResponse register(RequestResponse registrationRequest){
        RequestResponse requestResponse = new RequestResponse();
        try {
            User user = new User();
            user.setName(registrationRequest.getNom());
            requestResponse.setEmail(registrationRequest.getEmail());
            user.setEmail(registrationRequest.getEmail());
            requestResponse.setEmail(registrationRequest.getEmail());
            user.setRole(User.Role.UTILISATEUR);
            user.setPhone(registrationRequest.getPhone());
            requestResponse.setPhone(registrationRequest.getPhone());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setAge(registrationRequest.getAge());
            requestResponse.setAge(registrationRequest.getAge());
            User saved = userRepository.save(user);
            if(saved.getId()>0){
                requestResponse.setMessage("user saved successfully");
                requestResponse.setStatusCode(200);
            }

        }catch (Exception e){
            requestResponse.setStatusCode(500);
            requestResponse.setError(e.getMessage());
        }

        return requestResponse;
    }

    public RequestResponse login(RequestResponse loginRequest){
        RequestResponse requestResponse = new RequestResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generteRefreshToken(new HashMap<>(),user);
            requestResponse.setStatusCode(200);
            requestResponse.setToken(jwt);
            requestResponse.setRole(user.getRole().name());
            requestResponse.setRefreshToken(refreshToken);
            requestResponse.setExpirationTime("24Hrs");
            requestResponse.setMessage("user logged in successfully !");
            requestResponse.setNom(user.getName());
            requestResponse.setUserId(user.getId());
            requestResponse.setEmail(user.getEmail());
        }catch (Exception e){
            requestResponse.setStatusCode(500);
            requestResponse.setError(e.getMessage());
        }
        return requestResponse;
    }

    public void deleteUserFcmToken(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        user.setFcmToken(null);

        userRepository.save(user);
    }


    public List<UserResponseDTO> getAllUsersExcept(String email) {
        var currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userRepository.findAllByIdNot(currentUser.getId())
                .stream()
                .map(u -> new UserResponseDTO(u.getId(), u.getName()))
                .toList();
    }


}
