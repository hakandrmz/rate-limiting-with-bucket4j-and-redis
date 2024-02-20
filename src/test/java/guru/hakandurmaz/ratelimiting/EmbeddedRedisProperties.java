package guru.hakandurmaz.ratelimiting;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class EmbeddedRedisProperties {
    private final int redisPort;
    private final String redisHost;

    public EmbeddedRedisProperties(
            @Value("${redis.port:6379}") int redisPort, @Value("${redis.host:6379}") String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
}
