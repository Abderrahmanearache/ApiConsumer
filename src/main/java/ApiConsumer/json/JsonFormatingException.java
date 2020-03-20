package ApiConsumer.json;

public class JsonFormatingException extends Exception {

    public JsonFormatingException() {
    }

    public JsonFormatingException(String message) {
        super(message);
    }

    public JsonFormatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonFormatingException(Throwable cause) {
        super(cause);
    }

    public JsonFormatingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
