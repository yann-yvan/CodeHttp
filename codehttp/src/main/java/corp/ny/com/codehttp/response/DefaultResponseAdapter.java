package corp.ny.com.codehttp.response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yann-yvan on 19/11/17.
 */

public interface DefaultResponseAdapter {
    void toDefaultWithModel(JSONObject jsonObject, DefaultResponse defaultResponse) throws JSONException;
}
