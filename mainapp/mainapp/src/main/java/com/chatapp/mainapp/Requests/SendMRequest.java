package com.chatapp.mainapp.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SendMRequest {
    private Integer userId;
    private Integer chatId;
    private String content;
}
