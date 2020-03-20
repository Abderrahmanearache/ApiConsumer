package ApiConsumer.net;

public class StringResponse {
    private String response;

    public StringResponse(String response) {

        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return response;
    }
}
