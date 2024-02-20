package guru.hakandurmaz.ratelimiting.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "guru.otp.limiter")
@Data
public class RateLimiterProperties {
    private Integer limit;
    private Long ttl;
}

