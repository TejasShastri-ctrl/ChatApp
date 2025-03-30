package com.chatapp.mainapp.Repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapp.mainapp.Models.Message;
// 3:54, controller was finished before this. See if errors arise
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    @Query("Select m from Message m join m.chat c where c.id=:chatId")
    public List<Message> findByChatId(@Param("chatId")Integer chatId);
}
