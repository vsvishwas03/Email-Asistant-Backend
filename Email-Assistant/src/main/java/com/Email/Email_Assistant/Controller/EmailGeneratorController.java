package com.Email.Email_Assistant.Controller;


import com.Email.Email_Assistant.Entity.EmailRequest;
import com.Email.Email_Assistant.Service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailGeneratorController {
    @Autowired
    EmailGeneratorService egs;

    @PostMapping("/generate")
        public ResponseEntity<String> generateEmail(@RequestBody EmailRequest request){
           String response= egs.generteEmailReply(request);
            return ResponseEntity.ok(response);

        }


}
