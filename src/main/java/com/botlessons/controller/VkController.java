package com.botlessons.controller;

import com.botlessons.model.MessageNew;
import com.botlessons.model.ObjectMessage;
import com.botlessons.service.MessageSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VkController {
    private final String confirmationCode;
    private final MessageSenderService messageSenderService;

    public VkController(
            @Value("${confirmation.code}") String confirmationCode,
            MessageSenderService messageSenderService
    ) {
        this.confirmationCode = confirmationCode;
        this.messageSenderService = messageSenderService;
    }


    @PostMapping
    public String vkHandler(@RequestBody String groupEventJson) {
        System.out.println(groupEventJson);

        switch (getEventType(groupEventJson)) {
            case "confirmation":
                return confirmationCode;
            case "message_new":
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    MessageNew messageNew = objectMapper.readValue(groupEventJson, MessageNew.class);
                    ObjectMessage objectMessage = messageNew.getObject().getMessage();

                    int userId = objectMessage.getUserId();
                    String messageText = objectMessage.getText();
                    messageSenderService.send(userId, messageText);

                } catch (JsonProcessingException ex) {
                    System.out.println("groupEventJson not parsed: " + groupEventJson);
                    System.out.println();
                    System.out.println(ex.getMessage());
                }
                return "ok";
            default:
                return "ok";
        }
    }

    private String getEventType(String groupEventJson) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(groupEventJson);
        JsonObject rootJsonObject = jsonElement.getAsJsonObject();
        return rootJsonObject.get("type").getAsString();
    }

}
