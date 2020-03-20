package ApiConsumer.net;


public class ApiConsumerException extends Exception {

    public ApiConsumerException(Exception e) {
        super(e);
    }

    public ApiConsumerException() {
        super();
    }

    public ApiConsumerException(String message) {
        super(message);
    }

    public ApiConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiConsumerException(Throwable cause) {
        super(cause);
    }

    public ApiConsumerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
