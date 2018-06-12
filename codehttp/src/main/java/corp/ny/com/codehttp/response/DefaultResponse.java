package corp.ny.com.codehttp.response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import corp.ny.com.codehttp.models.PrepareRequest;


/**
 * Created by yann-yvan on 16/11/17.
 */

public class DefaultResponse<T> {
    private boolean status;
    private int message;
    private String token;
    private T model;
    private ArrayList<T> modelList;
    private String route;
    private PrepareRequest prepareRequest = new PrepareRequest();
    private HashMap family = new HashMap();

    public DefaultResponse(String route, JSONObject data, boolean isTokenRequired) throws JSONException {
        prepareRequest.setRoute(route);
        this.route = route;
        prepareRequest.setTokenRequired(isTokenRequired);
        if (data != null)
            prepareRequest.setOutgoing(data.toString());
    }

    public DefaultResponse(String route, boolean isTokenRequired) {
        prepareRequest.setRoute(route);
        prepareRequest.setTokenRequired(isTokenRequired);
        this.route = route;
    }

    public PrepareRequest getPrepareRequest() {
        return prepareRequest;
    }

    public void setPrepareRequest(PrepareRequest prepareRequest) {
        prepareRequest.setRoute(route);
        this.prepareRequest = prepareRequest;
    }

    public boolean isStatus() {
        return status;
    }

    public int getMessage() {
        return message;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getToken() {
        return token;
    }

    /**
     * Parse result to default class with data or token
     *
     * @param defaultResponseAdapter define custom way to manage response
     * @throws JSONException raise when wrong json format data is parse or when querying missing property
     */
    public void parseFromJson(DefaultResponseAdapter defaultResponseAdapter) throws JSONException {
        parseFromJson();
        defaultResponseAdapter.toDefaultWithModel(new JSONObject(prepareRequest.getIncoming()), this);
    }

    /**
     * Parse result to default class without child class
     *
     * @throws JSONException raise when wrong json format data is parse or when querying missing property
     */
    public void parseFromJson() throws JSONException {
        JSONObject jsonObject = new JSONObject(prepareRequest.getIncoming());
        int items = jsonObject.length();
        if (items < 2 || items > 4)
            throw new JSONException("Wrong response format " + jsonObject.toString(6));
        toDefault(jsonObject);
    }

    /**
     * @param jsonObject
     * @throws JSONException raise when wrong json format data is parse or when querying missing property
     */
    private void toDefault(JSONObject jsonObject) throws JSONException {
        this.status = jsonObject.getBoolean("status");
        this.message = jsonObject.getInt("message");
        if (jsonObject.has("token"))
            this.token = jsonObject.getString("token");
    }

    public void addModelToList(T model) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.add(model);
    }

    public ArrayList<T> getModelList() {
        return modelList;
    }

    public HashMap getFamily() {
        return family;
    }
}

