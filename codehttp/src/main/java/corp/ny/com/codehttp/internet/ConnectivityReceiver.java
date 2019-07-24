package corp.ny.com.codehttp.internet;

/**
 * Created by Lincoln on 18/03/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import corp.ny.com.codehttp.system.App;

public class ConnectivityReceiver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    public static List<ConnectivityReceiverListener> connectivityReceiverListeners = new ArrayList<>();

    public ConnectivityReceiver() {
        super();
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) App.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting() && isDataConnected();
        for (ConnectivityReceiverListener l : connectivityReceiverListeners) {
            if (l != null)
                l.onNetworkConnectionChanged(isConnected);
        }

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public Boolean isDataConnected() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 8.8.8.8");
            int returnVal = p1.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}