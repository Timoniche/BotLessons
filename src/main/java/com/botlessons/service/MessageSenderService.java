package com.botlessons.service;

import java.util.Random;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {
    private final GroupActor groupActor;
    private final VkApiClient vkApiClient;

    public MessageSenderService(GroupActor groupActor, VkApiClient vkApiClient) {
        this.groupActor = groupActor;
        this.vkApiClient = vkApiClient;
    }

    public void send(int userId, String message) {
        try {
            Random random = new Random();
            vkApiClient.messages()
                    .send(groupActor)
                    .message(message)
                    .userId(userId).randomId(random.nextInt())
                    .execute();
        } catch (ClientException | ApiException ignored) {
        }
    }
}
