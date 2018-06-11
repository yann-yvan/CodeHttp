package corp.ny.com.codehttp.system;

import android.app.Application;
import android.content.Context;

import corp.ny.com.codehttp.internet.ConnectivityReceiver;
import corp.ny.com.codehttp.utils.Debugger;
import corp.ny.com.codehttp.utils.ManifestReader;


/**
 * Created by yann-yvan on 16/11/17.
 * TO MAKE  THIS WORK PROPERLY YOU SHOULD ADD THIS CLASS
 * IN YOUR AndroidManifest.xml like this
 * <application android:name=".response.App">
 */
public class App extends Application {

    private static Context mContext;
    private static App mInstance;
    private Debugger debugger;

    public static Context getContext() {
        return mContext;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;
        if (ManifestReader.getMetadataBoolean("DEBUG_MODE"))
            debugger = new Debugger(mInstance);
    }

    public Debugger getDebugger() {
        return debugger;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}