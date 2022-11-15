package com.example.twilioemailsender.conf;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConf {

    @Value("${sendgrid.key}")
    private String key;

    @Bean
    public SendGrid getSendGrid(){
        return new SendGrid(key);
    }
}
