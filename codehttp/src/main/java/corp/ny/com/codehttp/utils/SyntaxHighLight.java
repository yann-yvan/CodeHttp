package corp.ny.com.codehttp.utils;

import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 08/06/18.
 */
public final class SyntaxHighLight {
    public static void highLightData(JSONObject jsonObject, WebView view) {
        if (jsonObject.has("html")) {
            try {
                view.loadData(jsonObject.getString("html"), "text/html; charset=utf-8", "utf-8");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            view.loadUrl(String.format("javascript:(function() {var e = document.createElement('code');\n" +
                    "    e.innerHTML =\"%s\";\n" +
                    "   document.getElementById(\"content\").appendChild(e);})()", browse(jsonObject, 0)));
    }

    private static String browse(JSONObject jsonObject, int tab) {
        String[] line = new String[jsonObject.length()];
        for (int i = 0; i < line.length; i++) {
            String key = null;

            //retrieve the key
            try {
                key = jsonObject.names().getString(i);
            } catch (JSONException e) {
                continue;
            }

            //check if it is an object
            try {
                JSONObject object = jsonObject.getJSONObject(key);
                line[i] = String.format("%s%s", tabulate(tab), formatNoValue(key, browse(object, key.length() + tab)));
                continue;
            } catch (JSONException ignored) {
            }

            //check if it is an array
            try {
                JSONArray object = jsonObject.getJSONArray(key);
                line[i] = String.format("%s%s", tabulate(tab), formatNoValue(key, browse(object, key.length() + tab)));
                continue;
            } catch (JSONException ignored) {
            }

            //get primitive object String, int, long, double, boolean
            try {
                line[i] = String.format("%s%s", tabulate(tab), format(key, jsonObject.get(key)));
            } catch (JSONException ignored) {
            }


        }
        return String.format("{%s%s%s}", indent(), TextUtils.join(",<br>", line), indentTabulate(tab));
    }

    private static String indent() {
        return "<br>";
    }

    private static String indentTabulate(int tab) {
        return indent() + tabulate(tab);
    }

    private static String tabulate(int tab) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tab; i++) {
            builder.append("&nbsp;");
        }
        return builder.toString();
    }


    private static String browse(JSONArray jsonArray, int tab) {
        String[] line = new String[jsonArray.length()];
        for (int i = 0; i < line.length; i++) {
            try {
                line[i] = browse(jsonArray.getJSONObject(i), tab);
            } catch (JSONException e) {
                try {
                    line[i] = browse(jsonArray.getJSONArray(i), tab);
                } catch (JSONException ignored) {
                }
            }
        }

        return String.format("[%s%s%s]", indent(), TextUtils.join(",<br>", line), indentTabulate(tab));
    }

    private static String formatNoValue(String key, String value) {
        return String.format("<span class=\\\"info\\\">\\\"%s\\\"</span>:%s", key, value);
    }

    private static String format(String key, Object value) {
        if (value instanceof String)
            return String.format("<span class=\\\"info\\\">\\\"%s\\\"</span>:<span class=\\\"warn\\\">\\\"%s\\\"</span>", key, value);
        else
            return String.format("<span class=\\\"info\\\">\\\"%s\\\"</span>:<span class=\\\"digit\\\">%s</span>", key, value);
    }
}
