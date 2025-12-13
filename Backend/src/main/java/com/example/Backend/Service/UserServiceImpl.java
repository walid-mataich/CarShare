package com.example.Backend.Service;

import com.example.Backend.Model.User;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.dto.RequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


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
            requestResponse.setEmail(user.getEmail());
        }catch (Exception e){
            requestResponse.setStatusCode(500);
            requestResponse.setError(e.getMessage());
        }
        return requestResponse;
    }

//    public RequestResponse refreshToken(RequestResponse refreshTokenRequest){
//        RequestResponse requestResponse = new RequestResponse();
//        try{
//            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
//            Administrateur admin = userRepository.findByEmail(email).orElseThrow();
//            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), admin)) {
//                var jwt = jwtUtils.generateToken(admin);
//                requestResponse.setStatusCode(200);
//                requestResponse.setToken(jwt);
//                requestResponse.setRefreshToken(refreshTokenRequest.getToken());
//                requestResponse.setExpirationTime("24Hr");
//                requestResponse.setMessage("Successfully Refreshed Token");
//            }
//            requestResponse.setStatusCode(200);
//            return requestResponse;
//
//        }catch (Exception e){
//            requestResponse.setStatusCode(500);
//            requestResponse.setMessage(e.getMessage());
//            return requestResponse;
//        }
//    }

}
