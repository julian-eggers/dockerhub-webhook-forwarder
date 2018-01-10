package com.itelg.docker.dwf.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.itelg.docker.dwf.strategy.forwarder.AwsSqsWebHookEventForwarder;
import com.itelg.docker.dwf.strategy.forwarder.WebHookEventForwarder;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnExpression("'${webhookevent.forward.awssqs.access-key}' != ''")
@Slf4j
@Setter
public class AwsSqsConfiguration
{
    @Value("${webhookevent.forward.awssqs.access-key}")
    private String accessKey;

    @Value("${webhookevent.forward.awssqs.secret-key}")
    private String secretKey;

    @Value("${webhookevent.forward.awssqs.region}")
    private String region;

    @Value("#{'${webhookevent.forward.awssqs.queues.original}'.split(',')}")
    private List<String> originalQueues;

    @Value("#{'${webhookevent.forward.awssqs.queues.compressed}'.split(',')}")
    private List<String> compressedQueues;

    @PostConstruct
    public void init()
    {
        Assert.isTrue(!originalQueues.isEmpty() || !compressedQueues.isEmpty(), "You have to specify at least one queue!");

        log.info("AwsSqs-Forwarder activated");
    }

    public AmazonSQSAsync awsSqsAsync()
    {
        AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        return AmazonSQSAsyncClientBuilder.standard().withRegion(resolveRegions(region)).withCredentials(credentials).build();
    }

    private static Regions resolveRegions(String region)
    {
        try
        {
            return Regions.valueOf(region);
        }
        catch (Exception e)
        {
            return Regions.fromName(region);
        }
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
