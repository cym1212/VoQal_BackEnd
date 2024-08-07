package Capstone.VoQal.global.error.error_message_discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordWebhook {
    private static final Logger logger = LoggerFactory.getLogger(DiscordWebhook.class);

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    public void sendLogToDiscord(String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 구분선을 포함한 메시지 생성
        String formattedMessage = String.format("```%s```", message);
        String jsonPayload = String.format("{\"content\":\"%s\"}", formattedMessage);
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Log sent to Discord successfully");
            } else {
                logger.error("Failed to send log to Discord: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while sending log to Discord", e);
        }
    }
}
