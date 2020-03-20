package ApiConsumer.net;

import java.net.URL;

public class JsonRequest extends Request<JsonRequest> {
    private OnFinish onFinish;
    private OnError onError;

    //<editor-fold desc="Constructors">
    public JsonRequest(URL url, Method method) {
        super(url, method);
    }

    public JsonRequest(String url) throws ApiConsumerException {
        super(url);
    }

    public JsonRequest(String url, Method method) throws ApiConsumerException {
        super(url, method);
    }

    public JsonRequest(URL url) {
        super(url);
    }

    //</editor-fold>

    //<editor-fold desc="Binding Listeners">

    public JsonRequest onFinish(OnFinish onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public JsonRequest onError(OnError onError) {
        this.onError = onError;
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="Override finish ans error">
    @Override
    protected void finish(String reponse) {
        if (onFinish == null)
            onFinish = System.out::println;

        try {
            onFinish.finish(new JsonResponse(reponse));
        } catch (JsonConsumerException e) {
            error(new JsonConsumerException(e));
        }
    }

    @Override
    protected void error(Exception e) {
        if (onError == null)
            onError = ex -> ex.printStackTrace();
        onError.error(e);

    }
    //</editor-fold>

    //<editor-fold desc="interfaces listeners">
    public interface OnFinish {
        void finish(JsonResponse response) throws JsonConsumerException;
    }

    public interface OnError {
        void error(Exception ex);
    }
    //</editor-fold>


}
