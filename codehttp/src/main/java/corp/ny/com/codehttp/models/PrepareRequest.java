package corp.ny.com.codehttp.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import corp.ny.com.codehttp.system.App;
import corp.ny.com.codehttp.utils.ManifestReader;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 30/05/18.
 */
public class PrepareRequest implements Serializable {
    private int code;
    private JSONObject incoming = new JSONObject();
    private String message;
    private String route;
    private boolean tokenRequired = false;
    private JSONObject outgoing = new JSONObject();
    private Method method;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getIncoming() {
        return incoming.toString();
    }

    public void setIncoming(String incoming) {
        try {
            this.incoming = new JSONObject(incoming);
        } catch (JSONException e) {
            try {
                this.incoming.put("html", incoming);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        App.getInstance().getDebugger().updateNotification(this);
    }

    public boolean isTokenRequired() {
        return tokenRequired;
    }

    public void setTokenRequired(boolean tokenRequired) {
        this.tokenRequired = tokenRequired;
    }

    public JSONObject getIncomingJsonObject() {
        return incoming;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = String.format("%s/%s", ManifestReader.getMetadataString("BASE_URL"), route);
    }

    public String getOutgoing() {
        return outgoing.toString();
    }

    public void setOutgoing(String outgoing) throws JSONException {
        this.outgoing = new JSONObject(outgoing);
    }

    public JSONObject getOutgoingJsonObject() {
        return outgoing;
    }

    public enum Method {
        POST,
        GET
    }
}
