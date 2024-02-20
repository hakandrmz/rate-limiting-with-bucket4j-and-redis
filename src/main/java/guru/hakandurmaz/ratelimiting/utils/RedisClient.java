package guru.hakandurmaz.ratelimiting.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {
    private final RedisTemplate redisStringTemplate;

    public RedisClient(@Qualifier("redisStringTemplate") RedisTemplate redisStringTemplate) {
        this.redisStringTemplate = redisStringTemplate;
    }

    public String get(String key) {
        final var value = redisStringTemplate.opsForValue()
                .get(key);
        return value != null ? value.toString() : null;
    }

    public boolean set(String key, String value, long ttl) {
        this.redisStringTemplate.opsForValue()
                .set(key, value, ttl, TimeUnit.MILLISECONDS);
        return true;
    }

    public void clean() {
        Set<String> keys = redisStringTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisStringTemplate.delete(keys);
        }
    }
}
