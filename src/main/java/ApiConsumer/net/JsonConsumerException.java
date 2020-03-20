package ApiConsumer.net;


public class JsonConsumerException extends Exception {
    public JsonConsumerException(Exception e) {
        super(e);
    }

    public JsonConsumerException() {
        super();
    }

}
