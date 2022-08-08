package com.github.daianaegermichels.zapzap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class RabbitMQConfig {

    //fifth: create rabbit admin to manage creation of new queues
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    //fourth: convert messages to JSON to avoid read errors
//    @Bean
//    MessageConverter jsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }
    //modify Jackson to receive date of type LocalDateTime:
    @Bean
    MessageConverter jsonMessageConverter(Jackson2ObjectMapperBuilder builder){
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    //first: define an exchange
    @Bean
    DirectExchange privateChat(){
        return new DirectExchange("privatechat");
    }

    //sixth: create a group chat
    @Bean
    TopicExchange groupChat(){
        return new TopicExchange("groupdevin");
    }

    //second: create queue that will listen for messages to be sent
    //durable: defines that the queue will be restarted after an application restart
    @Bean
    Queue queuePrivateChat(){
        return QueueBuilder.durable("devin.chat.daiana.queue").build();
    }

    //third: define a binding to make the connection between the exchange and the queue
    @Bean
    Binding bindingPrivateChat(Queue queuePrivateChat, DirectExchange privateChat){
        return BindingBuilder.bind(queuePrivateChat).to(privateChat)
                .with("devin.chat.daiana");
    }

    @Bean
    Binding bindingGroupChat(Queue queuePrivateChat, TopicExchange groupChat){
        return BindingBuilder.bind(queuePrivateChat).to(groupChat)
                .with("devin.chat.*");
    }



}
