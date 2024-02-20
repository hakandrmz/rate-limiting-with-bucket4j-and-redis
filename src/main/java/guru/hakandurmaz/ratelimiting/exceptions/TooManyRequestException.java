package guru.hakandurmaz.ratelimiting.exceptions;

public class TooManyRequestException extends RuntimeException {

    public TooManyRequestException(String message) {
        super(message);
    }
}
