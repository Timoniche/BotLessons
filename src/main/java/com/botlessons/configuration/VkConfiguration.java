package com.botlessons.configuration;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkConfiguration {

    @Bean
    public GroupActor groupActor(
            @Value("${vk.token}") String token,
            @Value("${group.id}") String groupIdAsString
    ) {
        try {
            int groupId = Integer.parseInt(groupIdAsString);
            return new GroupActor(groupId, token);
        } catch (NumberFormatException ex) {
            throw new AssertionError(groupIdAsString + " must be int");
        }
    }

    @Bean
    public VkApiClient vkApiClient() {
        HttpTransportClient client = new HttpTransportClient();
        return new VkApiClient(client);
    }
}
