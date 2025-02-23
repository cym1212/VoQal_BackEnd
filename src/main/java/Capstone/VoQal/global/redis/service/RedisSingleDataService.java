package Capstone.VoQal.global.redis.service;

import java.time.Duration;

public interface RedisSingleDataService {
    int setSingleData(String key, Object value);

    int setSingleData(String key, String value, Duration duration);

    String getSingleData(String key);

    int deleteSingleData(String key);
}
