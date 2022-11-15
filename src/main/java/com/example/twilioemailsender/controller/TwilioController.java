package com.example.twilioemailsender.controller;

import com.example.twilioemailsender.request.EmailRequest;
import com.example.twilioemailsender.service.TwilioService;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwilioController {


    @Autowired
    public final TwilioService twilioService;

    public TwilioController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }


    @PostMapping("/sendMails")
    public ResponseEntity<String> sendTextEmail(@RequestBody EmailRequest emailRequest){

        Response response=twilioService.sendTextEmail(emailRequest);
         if(response.getStatusCode()==200 || response.getStatusCode()==202){
            return new ResponseEntity<>("Email sends Successfully", HttpStatus.OK);
        }
        return  new ResponseEntity<>("Failed to sends the email",HttpStatus.NOT_FOUND);
    }
}
