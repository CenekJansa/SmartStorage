package com.example.SecureStorage.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.config.RabbitMQConfig;

@Service
public class DocumentProcessingProducer {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Send a document processing message to RabbitMQ
     * 
     * @param message the document processing message
     */
    public void sendProcessingMessage(DocumentProcessingMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            message
        );
    }
}

