package guru.hakandurmaz.ratelimiting.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public enum RequestUtils {
    ;
    public static final String NO_VALUE_PRESENT = "No value present";
    private static final String FORWARDED_FOR_HEADER_KEY = "X-Forwarded-For";

    public static String getClientIPAddress(final HttpServletRequest httpServletRequest) {
        return Optional.of(httpServletRequest)
                .map(request -> Objects.nonNull(request.getHeader(FORWARDED_FOR_HEADER_KEY)) ?
                        request.getHeader(FORWARDED_FOR_HEADER_KEY) :
                        request.getRemoteAddr())
                .orElseThrow(() -> new NoSuchElementException(NO_VALUE_PRESENT));
    }
}
