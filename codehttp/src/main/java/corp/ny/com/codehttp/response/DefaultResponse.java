package corp.ny.com.codehttp.response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.models.Token;


/**
 * Created by yann-yvan on 16/11/17.
 */

public class DefaultResponse<T> {
    private boolean status;
    private int message;
    private T model;
    private ArrayList<T> modelList;
    private String route;
    private PrepareRequest prepareRequest = new PrepareRequest();
    private HashMap family = new HashMap();
    private List<FormPart> formParts = new ArrayList<>();

    public DefaultResponse(String route, JSONObject data, boolean isTokenRequired) throws JSONException {
        if (data != null)
            prepareRequest.setOutgoing(data.toString());
        initialize(route, isTokenRequired);
    }

    public DefaultResponse(PrepareRequest prepareRequest) {
        this.prepareRequest = prepareRequest;
    }

    /**
     * Handle Laravel pagination
     *
     * @return
     */
    public boolean canPaginateLaravel() {
        JSONObject jsonObject = prepareRequest.getIncomingJsonObject();
        int nextPage = findPagination(jsonObject);
        if (nextPage > 1) {
            try {
                prepareRequest.setOutgoing(prepareRequest.getOutgoingJsonObject().put("page", nextPage).toString());
            } catch (JSONException ignore) {
            }
            return true;
        }
        return false;
    }

    private int findPagination(JSONObject jsonObject) {
        String nextPageUrl = "next_page_url";
        String currentPage = "current_page";
        String lastPage = "last_page";
        int nextPage = -1;
        for (int i = 0; i < jsonObject.length(); i++) {

            //retrieve the key
            try {
                String key = jsonObject.names().getString(i);
                //Log.e("Key", key);
                if (key.equalsIgnoreCase(nextPageUrl)) {
                    // Log.e("Target", "Found");
                    if (jsonObject.getString(nextPageUrl) != null &&
                            jsonObject.getInt(lastPage) > jsonObject.getInt(currentPage)) {
                        nextPage = jsonObject.getInt(currentPage) + 1;
                    }
                    return nextPage;
                }
                //check if it is an object
                nextPage = findPagination(jsonObject.getJSONObject(key));
            } catch (JSONException ignore) {

            }
        }
        return nextPage;
    }

    public DefaultResponse(String route, boolean isTokenRequired) {
        initialize(route, isTokenRequired);
    }

    private void initialize(String route, boolean isTokenRequired) {
        prepareRequest.setRoute(route);
        prepareRequest.setTokenRequired(isTokenRequired);
        this.route = route;
    }

    public void addFile(FileType mediaType, String property, String fileName, String path) {
        formParts.add(new FormPart(mediaType.toString(), property, fileName, path));
    }

    public List<FormPart> getFormParts() {
        return formParts;
    }

    public PrepareRequest getPrepareRequest() {
        return prepareRequest;
    }

    public void setPrepareRequest(PrepareRequest prepareRequest) {
        prepareRequest.setRoute(route);
        prepareRequest.setTokenRequired(this.prepareRequest.isTokenRequired());
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
        return Token.getToken();
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
            Token.setToken(jsonObject.getString("token"));
    }

    /* public ArrayList<T> toModelList(T model) {
         Moshi moshi = new Moshi.Builder().build();
         //System.out.println(jsonObject.getJSONObject("user").toString(5));
         JsonAdapter<T> adapter = moshi.adapter(T);
         //deserialize the json

     }
 */
    public void addModelToList(T model) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.add(model);
    }

    /**
     * Reset store token
     */
    public void resetToken() {
        Token.reset();
    }

    public ArrayList<T> getModelList() {
        return modelList;
    }

    public HashMap getFamily() {
        return family;
    }
}

