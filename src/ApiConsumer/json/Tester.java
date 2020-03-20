package ApiConsumer.json;

import ApiConsumer.net.ApiConsumerException;
import ApiConsumer.net.JsonRequest;
import ApiConsumer.net.Method;
import ApiConsumer.net.Request;

public class Tester {
    public static void main(String[] args) throws ApiConsumerException, JsonFormatingException {
        Request request = new JsonRequest("http://localhost:8080/app/ana", Method.POST)
                .onFinish(response -> System.out.println(response))
                .addJsonParam(new Data())
                .onError(System.err::println);

        request.execute();
    }

    static class Data {
        String key = "value";
        String key2 = "value2";
    }
}
