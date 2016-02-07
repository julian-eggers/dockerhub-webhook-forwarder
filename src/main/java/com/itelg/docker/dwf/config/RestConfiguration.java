package com.itelg.docker.dwf.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class RestConfiguration extends WebMvcConfigurerAdapter
{
    @Autowired
    private XStreamMarshaller xStreamMarshaller;
    
    @Bean
    public MarshallingHttpMessageConverter marshallingHttpMessageConverter()
    {
        MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_XML));
        xmlConverter.setMarshaller(xStreamMarshaller);
        xmlConverter.setUnmarshaller(xStreamMarshaller);
        return xmlConverter;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.TEXT_XML);
        configurer.favorPathExtension(true);
        configurer.mediaType("xml", MediaType.TEXT_XML);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.add(marshallingHttpMessageConverter());
    }
}