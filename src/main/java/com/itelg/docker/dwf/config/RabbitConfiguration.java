package com.itelg.docker.dwf.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnExpression("'${webhookevent.forward.rabbitmq.hosts}' != ''")
@Slf4j
public class RabbitConfiguration
{
    @Value("${webhookevent.forward.rabbitmq.hosts}")
    private String rabbitmqAddresses;

    @Value("${webhookevent.forward.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${webhookevent.forward.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${webhookevent.forward.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${webhookevent.forward.rabbitmq.routing-key.prefix}")
    private String routingKeyPrefix;

    @PostConstruct
    public void init()
    {
        log.info("RabbitMQ-Forwarder activated");
    }

    @Bean
    public CachingConnectionFactory connectionFactory()
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitmqAddresses);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin()
    {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public TopicExchange webHookEventExchange()
    {
        TopicExchange exchange = new TopicExchange(exchangeName);
        rabbitAdmin().declareExchange(exchange);
        return exchange;
    }

    @Bean
    public RabbitTemplate webHookEventTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(exchangeName);
        rabbitTemplate.setRoutingKey(routingKeyPrefix + ".compressed");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate webHookEventOriginalTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(exchangeName);
        rabbitTemplate.setRoutingKey(routingKeyPrefix + ".original");
        return rabbitTemplate;
    }
}