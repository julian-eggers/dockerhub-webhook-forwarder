package com.itelg.docker.dwf.config;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration
{
    @Autowired
    private ConnectionFactory connectionFactory;

    @NotBlank
    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @NotBlank
    @Value("${spring.rabbitmq.routing-key.prefix}")
    private String routingKeyPrefix;

    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public TopicExchange eventExchange()
    {
        TopicExchange exchange = new TopicExchange(exchangeName);
        rabbitAdmin().declareExchange(exchange);
        return exchange;
    }

    @Bean
    public RabbitTemplate webhookEventTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchangeName);
        rabbitTemplate.setRoutingKey(routingKeyPrefix + ".compressed");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate webhookEventOriginalTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchangeName);
        rabbitTemplate.setRoutingKey(routingKeyPrefix + ".original");
        return rabbitTemplate;
    }
}