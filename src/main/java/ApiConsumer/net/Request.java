package ApiConsumer.net;

import ApiConsumer.json.Json;
import ApiConsumer.json.JsonFormatingException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public abstract class Request<T> implements ApiConsumer<T> {
    protected HttpURLConnection connection;
    protected URL url;
    protected Method method;
    protected String reponse;
    protected String data;
    protected boolean canceled = false;
    protected Thread netAccesThread;
    protected boolean jsonParam = false;

    //<editor-fold desc="Constructors">

    public Request(URL url, Method method) {
        this.url = url;
        this.method = method;
        data = "";
        reponse = "";
    }

    public Request(String url) throws ApiConsumerException {
        this(url, Method.GET);
    }

    public Request(String url, Method method) throws ApiConsumerException {
        try {
            this.url = new URL(url);
            this.method = method;
            data = "";
            reponse = "";
        } catch (MalformedURLException e) {
            throw new ApiConsumerException(e);
        }
    }

    public Request(URL url) {
        this(url, Method.GET);
    }

    //</editor-fold>

    //<editor-fold desc="Executors">

    public void execute() {
        netAccesThread = new Thread(() -> {
            try {
                backgroundJob();
            } catch (Exception e) {
                error(new ApiConsumerException(e.getMessage()));
            }
        });
        netAccesThread.start();
    }

    public void doPost() {
        method = Method.POST;
        execute();
    }

    public void doRequest() {
        doPost();
    }

    public void doGet() {
        method = Method.GET;
        execute();
    }

    public void cancel() {
        canceled = true;
    }

    //</editor-fold>

    //<editor-fold desc="Binding Data">

    public T addParams(Map<String, String> map) {
        StringBuilder data = new StringBuilder();
        try {
            for (Map.Entry<String, String> param : map.entrySet()) {
                if (data.length() != 0) data.append('&');
                data.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.data = data.toString();
        return (T) this;

    }

    public T addParams(String[][] datas) {
        StringBuilder data = new StringBuilder();

        try {
            for (String[] param : datas) {
                if (data.length() != 0) data.append('&');
                data.append(URLEncoder.encode(param[0], "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(param[1], "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.data = data.toString();

        return (T) this;

    }

    public String getData() {
        return data;
    }

    public T addJsonParam(Object o) throws JsonFormatingException {
        data = new String(Json.from(o).getBytes(StandardCharsets.UTF_8));
        jsonParam = true;

        return (T) this;
    }

    //</editor-fold>

    //<editor-fold desc="All those functions work perfectly in the background">

    private void backgroundJob() throws Exception {
        if (jsonParam)
            method = Method.POST;

        if (url.toExternalForm().contains("?"))
            throw new ApiConsumerException("Trying to pass parameters within URL, not allowed, try addParams function");


        if (method == Method.GET && !getData().isEmpty())
            url = new URL(url.toExternalForm() + "?" + getData());

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.toString());

        if (jsonParam)
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

        connection.setRequestProperty("User-Agent", "");

        sendData();
        receaveData();
        if (!canceled)
            finish(reponse);
    }

    private void receaveData() throws IOException, ApiConsumerException {

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                if (reponse.length() != 0)
                    reponse += "\n";
                reponse += scanner.nextLine();
            }
        } else

            throw new ApiConsumerException(new Exception(
                    connection.getResponseCode() + " : " + connection.getResponseMessage())
            );
    }

    private void sendData() throws IOException {
        if (method == Method.POST && !getData().isEmpty()) {
            connection.setDoOutput(true);
            PrintStream ps = new PrintStream(connection.getOutputStream());
            ps.print(getData());
            ps.flush();
        }
    }

    //</editor-fold>

    //<editor-fold desc="Abstract methods , for listener">

    protected abstract void finish(String reponse);

    protected abstract void error(Exception e);

    //</editor-fold>


}
