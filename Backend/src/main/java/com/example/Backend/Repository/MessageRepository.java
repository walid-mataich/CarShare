package com.example.Backend.Repository;

import com.example.Backend.Model.Message;
import com.example.Backend.Model.User;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderBySentAtAsc(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
    );

}
