package com.example.twilioemailsender.service;

import com.example.twilioemailsender.request.EmailRequest;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class TwilioService {


    @Autowired
    private final SendGrid sendGrid;

    public TwilioService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }


    @Value("${spring.kafka.topic.name}")
    private String topicName;


    @Value("${sendgrid.key}")
    private String key;

    @KafkaListener(topics = "${spring.kafka.topic.name}"
            , groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, String> payload) {
        log.info("Topic: {}", topicName);
        log.info("key: {}", payload.key());
        log.info("Headers: {}", payload.headers());
        log.info("Partition: {}", payload.partition());
        log.info("Order: {}", payload.value());

        String emailString = payload.value();
        List<String> mailId = Arrays.asList(emailString);
        log.info("Array as list" + mailId.toString());

        for (String mails : mailId) {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(mails);

            Date date = new Date();
            Response response = sendTextEmail(emailRequest);
        }
    }

//        String value = payload.value();
//        String[] s = value.split(",", 5);

//        for (String s1 : s) {
//            String to = s1.replace("email=", "");
//            try {
//                sendTextEmail(to);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }

    public Response sendTextEmail(EmailRequest emailrequest) {

        Mail mail = new Mail(new Email("saurabhkanawade30@gmail.com"), emailrequest.getSubject(),
                new Email(emailrequest.getTo()), new Content("text/plain",
                emailrequest.getBody()));

        mail.setReplyTo(new Email());
        Request request = new Request();

        Response response = null;

        try {

            request.setMethod(Method.POST);

            request.setEndpoint("mail/send");

            request.setBody(mail.build());

            response = this.sendGrid.api(request);

        } catch (IOException ex) {

            System.out.println(ex.getMessage());

        }

        log.info("Email is sends successfully");
        return response;

    }
}
