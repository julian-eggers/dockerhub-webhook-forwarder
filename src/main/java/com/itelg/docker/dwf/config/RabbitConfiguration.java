package com.itelg.docker.dwf.config;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MarshallingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;


@Configuration
public class RabbitConfiguration
{
    @Autowired
    private ConnectionFactory connectionFactory;
    
    @Autowired
    private XStreamMarshaller xStreamMarshaller;
    
    @NotBlank
    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;
    
    @NotBlank
    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;
    
    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        return new RabbitAdmin(connectionFactory);
    }
    
    @Bean
    public TopicExchange eventExchange()
    {
        TopicExchange exchange = new TopicExchange(exchangeName);
        rabbitAdmin().declareExchange(exchange);
        return exchange;
    }
    
    @Bean
    public RabbitTemplate eventPublishTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(marshallingMessageConverter());
        rabbitTemplate.setExchange(exchangeName);
        rabbitTemplate.setRoutingKey(routingKey);
        
        return rabbitTemplate;
    }
    
    @Bean
    public MessageConverter marshallingMessageConverter()
    {
        MarshallingMessageConverter messageConverter = new MarshallingMessageConverter();
        messageConverter.setMarshaller(xStreamMarshaller);
        messageConverter.setUnmarshaller(xStreamMarshaller);
        return messageConverter;
    }
}