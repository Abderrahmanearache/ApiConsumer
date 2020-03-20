package ApiConsumer.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonResponse extends Response {

    private boolean isJson = true;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private String json;
    private boolean isArray = false;
    private boolean isObject = false;

    public JsonResponse(String jsonValue) throws JsonConsumerException {

        this.json = jsonValue.trim();

        try {
            if (this.json.startsWith("[")) {
                isArray = true;
                jsonArray = new JSONArray(this.json);
            } else if (this.json.startsWith("{")) {
                isObject = true;
                jsonObject = new JSONObject(this.json);
            } else {
                throw new JsonConsumerException(new Exception("This is not a Json content , verify your API "));
            }
        } catch (JSONException | JsonConsumerException e) {
            throw new JsonConsumerException(e);
        }
    }

    public boolean isObject() {
        return isObject;
    }

    public boolean isJson() {
        return isJson;
    }

    public boolean isArray() {
        return isArray;
    }

    public String getJsonValue() {
        return json;
    }

    public JSONArray getJsonArray() throws JsonConsumerException {
        if (isArray)
            return jsonArray;
        else
            throw new JsonConsumerException(new Exception("this is not a Json Array !! "));
    }

    public JSONObject getJsonObject() throws JsonConsumerException {

        if (isObject)
            return jsonObject;
        else
            throw new JsonConsumerException(new Exception("this is not a Json Object !! "));
    }

    @Override
    public String toString() {
        return getJsonValue();
    }


}
