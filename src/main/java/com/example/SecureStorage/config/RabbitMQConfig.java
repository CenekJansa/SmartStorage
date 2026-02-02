package com.example.SecureStorage.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String QUEUE_NAME = "document-processing-queue";
    public static final String EXCHANGE_NAME = "document-processing-exchange";
    public static final String ROUTING_KEY = "document-processing";
    
    @Bean
    public Queue documentProcessingQueue() {
        return new Queue(QUEUE_NAME, true); // durable queue
    }
    
    @Bean
    public DirectExchange documentProcessingExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Binding binding(Queue documentProcessingQueue, DirectExchange documentProcessingExchange) {
        return BindingBuilder.bind(documentProcessingQueue)
                .to(documentProcessingExchange)
                .with(ROUTING_KEY);
    }
    
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

