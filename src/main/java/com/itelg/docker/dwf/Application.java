package com.itelg.docker.dwf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

import com.avides.spring.prometheus.client.configuration.EnablePrometheusMetrics;

@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
@EnablePrometheusMetrics
public class Application
{
    public static final void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}