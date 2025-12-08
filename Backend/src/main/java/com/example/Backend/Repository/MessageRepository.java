package com.example.Backend.Repository;

import com.example.Backend.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message,Long> {
}
