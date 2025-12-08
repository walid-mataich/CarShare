package com.example.Backend.Service;



import com.example.Backend.Model.User;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User updateProfile(Long id, User updated);
}
