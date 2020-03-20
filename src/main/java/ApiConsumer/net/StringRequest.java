package ApiConsumer.net;

import java.net.URL;

public class StringRequest extends Request<StringRequest> {
    private OnFinish onFinish;
    private OnError onError;

    //<editor-fold desc="Constructors">
    public StringRequest(URL url, Method method) {
        super(url, method);
    }

    public StringRequest(String url) throws ApiConsumerException {
        super(url);
    }

    public StringRequest(String url, Method method) throws ApiConsumerException {
        super(url, method);
    }

    public StringRequest(URL url) {
        super(url);
    }

    //</editor-fold>

    //<editor-fold desc="Binding Listeners">

    public StringRequest onFinish(OnFinish onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public StringRequest onError(OnError onError) {
        this.onError = onError;
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="Override finish ans error">
    @Override
    protected void finish(String reponse) {
        if (onFinish == null)
            onFinish = System.out::println;

        onFinish.finish(new StringResponse(reponse));

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
        void finish(StringResponse response);
    }

    public interface OnError {
        void error(Exception ex);
    }
    //</editor-fold>


}
