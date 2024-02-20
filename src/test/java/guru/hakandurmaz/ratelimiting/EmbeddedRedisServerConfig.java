package guru.hakandurmaz.ratelimiting;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class EmbeddedRedisServerConfig {
    private final EmbeddedRedisProperties redisProperties;
    private RedisServer redisServer;

    public EmbeddedRedisServerConfig(EmbeddedRedisProperties redisProperties) throws IOException {
        this.redisProperties = redisProperties;
        this.redisServer = new RedisServer(redisProperties.getRedisPort());
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        try {
            redisServer.start();
        } catch (IOException exception) {
            this.redisServer = new RedisServer(redisProperties.getRedisPort());
        }
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }
}
