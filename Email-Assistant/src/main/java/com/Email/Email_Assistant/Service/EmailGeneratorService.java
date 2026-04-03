package com.Email.Email_Assistant.Service;


import com.Email.Email_Assistant.Entity.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service

public class EmailGeneratorService {


    private WebClient client;
    @Value("${gemini.api.url}")
    private String geminiApiURL;
    @Value("${gemini.api.key}")
    private String geminiApiKey;


    public EmailGeneratorService() {
        this.client = WebClient.builder().build();
    }

    //call google gemini api
    public String generteEmailReply(EmailRequest request){

        //Build prompt
         String
                 prompt= buildPrompt(request);
        // create a api request for gemini api
        Map<String, Object> requestBody=Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );
        //do request and get response
        String response= client.post()
                .uri(geminiApiURL)
                .header("Content-Type","application/json")
                .header("X-goog-api-key", geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //extract and return response

        return extractResponse(response);
    }

    private String extractResponse(String response) {
        try{
            ObjectMapper mapper= new ObjectMapper();
            JsonNode rootNode= mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "error Processing Request: " + e.getMessage();
        }


    }

    private String buildPrompt(EmailRequest request) {
        StringBuilder prompt= new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. please don't generate a subject line ");
        if(request.getTone()!=null && !request.getTone().isEmpty()){
            prompt.append("Use a ").append(request.getTone()).append(" tone. ");

        }
        prompt.append("\n Original email: \n").append(request.getEmailContent());


        return prompt.toString() ;

    }
}
