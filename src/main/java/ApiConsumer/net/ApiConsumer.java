package ApiConsumer.net;

import ApiConsumer.json.JsonFormatingException;

import java.util.Map;

public interface ApiConsumer<T> {

    void cancel();

    void execute();

    void doPost();

    void doRequest();

    void doGet();

    T addParams(Map<String, String> map);

    T addParams(String[][] datas);

    T addJsonParam(Object o) throws JsonFormatingException;

}
