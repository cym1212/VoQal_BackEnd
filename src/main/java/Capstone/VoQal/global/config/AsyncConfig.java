package Capstone.VoQal.global.config;

import Capstone.VoQal.global.error.AsyncCustomExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncCustomExceptionHandler();
    }

    @Override
    public Executor getAsyncExecutor() {
        return AsyncConfigurer.super.getAsyncExecutor();
    }
}
