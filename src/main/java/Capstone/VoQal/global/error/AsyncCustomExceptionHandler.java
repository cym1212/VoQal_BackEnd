package Capstone.VoQal.global.error;

import Capstone.VoQal.global.error.error_message_discord.DiscordWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class AsyncCustomExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsyncCustomExceptionHandler.class);
    private final DiscordWebhook discordWebhook = new DiscordWebhook();

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        String errorMessage = String.format("Exception message: %s, Method name: %s, Parameters: %s",
                ex.getMessage(), method.getName(), params);
        logger.error(errorMessage);
        discordWebhook.sendLogToDiscord(errorMessage);
    }
}
