package com.itelg.docker.dwf.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.itelg.docker.dwf.strategy.forwarder.AwsSqsWebHookEventForwarder;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnExpression("'${webhookevent.forward.awssqs.access-key}' != ''")
@Slf4j
public class AwsSqsConfiguration
{
    @Value("${webhookevent.forward.awssqs.access-key}")
    private String accessKey;

    @Value("${webhookevent.forward.awssqs.secret-key}")
    private String secretKey;

    @Value("${webhookevent.forward.awssqs.region}")
    private String region;

    @PostConstruct
    public void init()
    {
        log.info("AwsSqs-Forwarder activated");
    }

    public AmazonSQSAsync awsSqsAsync()
    {
        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        return AmazonSQSAsyncClientBuilder.standard().withRegion(region).withCredentials(credentials).build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate()
    {
        return new QueueMessagingTemplate(awsSqsAsync());
    }

    @Bean
    public WebHookEventForwarder awsSqsWebhookEventForwarder()
    {
        return new AwsSqsWebHookEventForwarder();
    }
}
