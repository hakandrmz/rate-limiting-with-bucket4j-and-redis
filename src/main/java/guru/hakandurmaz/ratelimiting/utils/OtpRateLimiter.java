package guru.hakandurmaz.ratelimiting.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.hakandurmaz.ratelimiting.properties.RateLimiterProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class OtpRateLimiter {
    private final static long INITIAL_HIT_COUNT = 1L;
    private final static String CACHE_KEY_PREFIX = "otp_rate_limiting";
    private final Logger logger = LoggerFactory.getLogger(OtpRateLimiter.class);
    private final ObjectMapper objectMapper;
    private final RateLimiterProperties rateLimiterProperties;
    private final RedisClient redisClient;

    public OtpRateLimiter(ObjectMapper objectMapper,
                          RateLimiterProperties rateLimiterProperties,
                          RedisClient redisClient) {
        this.objectMapper = objectMapper;
        this.rateLimiterProperties = rateLimiterProperties;
        this.redisClient = redisClient;
    }

    public boolean isBlocked(String clientIPAddress) {
        try {
            final var redisKey = String.join("_", CACHE_KEY_PREFIX, clientIPAddress);
            final Duration duration = Duration.ofMillis(rateLimiterProperties.getTtl());
            final Bucket bucket = buildBucket(redisKey, rateLimiterProperties.getLimit(), duration);
            if (bucket.tryConsume(INITIAL_HIT_COUNT)) {
                final String bucketJson = buildBucketJson(bucket);
                redisClient.set(redisKey, bucketJson, rateLimiterProperties.getTtl());
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("Error while rate limiting", e);
        }
        return false;
    }

    private Bucket buildBucket(
            final String key,
            final Integer requestLimit,
            final Duration period
    ) throws IOException {
        final String bucketJson = redisClient.get(key);
        return Objects.nonNull(bucketJson)
                ? parseBucket(bucketJson)
                : Bucket
                .builder()
                .addLimit(
                        Bandwidth.classic(
                                requestLimit,
                                Refill.intervally(requestLimit, period)
                        )
                )
                .build();
    }

    private Bucket parseBucket(final String bucketJson) throws IOException {
        @SuppressWarnings("unchecked") final Map<String, Object> bucketMap = objectMapper.readValue(
                bucketJson,
                Map.class
        );
        return LocalBucket.fromJsonCompatibleSnapshot(bucketMap);
    }

    private String buildBucketJson(final Bucket bucket) throws IOException {
        return objectMapper.writeValueAsString(
                ((LocalBucket) bucket).toJsonCompatibleSnapshot()
        );
    }
}
