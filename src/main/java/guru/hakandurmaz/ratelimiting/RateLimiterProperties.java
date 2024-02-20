package guru.hakandurmaz.ratelimiting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "guru.otp.limiter")
@Data
public class RateLimiterConfiguration {

    private Integer limit;

    private Long ttl;
}

