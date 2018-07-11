package corp.ny.com.codehttp.models;

import android.content.SharedPreferences;

import corp.ny.com.codehttp.R;
import corp.ny.com.codehttp.system.App;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 31/05/18.
 */
public class Token {

    public static String getToken() {
        return getPreference().getString(Token.class.getSimpleName(), null);
    }

    public static void setToken(String token) {
        getPreference().edit().putString(Token.class.getSimpleName(), token).apply();
    }

    public static void reset() {
        getPreference().edit().putString(Token.class.getSimpleName(), null).apply();
    }

    private static SharedPreferences getPreference() {
        return App.getContext().getSharedPreferences(App.getContext().getString(R.string.app_name), MODE_PRIVATE);
    }
}
