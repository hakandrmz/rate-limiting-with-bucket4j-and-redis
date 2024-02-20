package guru.hakandurmaz.ratelimiting.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.hakandurmaz.ratelimiting.exceptions.TooManyRequestException;
import guru.hakandurmaz.ratelimiting.properties.RateLimiterProperties;
import guru.hakandurmaz.ratelimiting.utils.OtpRateLimiter;
import guru.hakandurmaz.ratelimiting.utils.RedisClient;
import guru.hakandurmaz.ratelimiting.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class SendOtpRateLimiterInterceptor implements HandlerInterceptor {
    private final OtpRateLimiter otpRateLimiter;

    public SendOtpRateLimiterInterceptor(ObjectMapper objectMapper, RateLimiterProperties rateLimiterProperties,
                                         RedisClient redisClient) {
        this.otpRateLimiter = new OtpRateLimiter(objectMapper, rateLimiterProperties, redisClient);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String clientIPAddress = RequestUtils.getClientIPAddress(request);
        if (clientIPAddress == null || clientIPAddress.isEmpty()) {
            clientIPAddress = "UNKNOWN";
        }
        final var isBlocked = otpRateLimiter.isBlocked(clientIPAddress);
        if (isBlocked) throw new TooManyRequestException("Too many requests from " + clientIPAddress);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
