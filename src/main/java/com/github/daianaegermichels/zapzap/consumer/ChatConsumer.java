package com.github.daianaegermichels.zapzap.consumer;

import com.github.daianaegermichels.zapzap.dto.ChatMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ChatConsumer {
    //class that will listen for messages

    //anoannotation to listen to a queue @RabbitListener
    @RabbitListener(queues= "devin.chat.daiana.queue")
    public void getPrivateMessage(ChatMessage chatMessage){
        System.out.println(chatMessage);
    }
}
