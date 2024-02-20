package guru.hakandurmaz.ratelimiting.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.hakandurmaz.ratelimiting.interceptors.SendOtpRateLimiterInterceptor;
import guru.hakandurmaz.ratelimiting.properties.RateLimiterProperties;
import guru.hakandurmaz.ratelimiting.utils.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RateLimiterInterceptorConfig implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;
    private final RedisClient redisClient;
    private final RateLimiterProperties rateLimiterProperties;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SendOtpRateLimiterInterceptor(objectMapper, rateLimiterProperties, redisClient))
                .addPathPatterns("/api/send-otp/**")
                .pathMatcher(new AntPathMatcher());
    }
}
