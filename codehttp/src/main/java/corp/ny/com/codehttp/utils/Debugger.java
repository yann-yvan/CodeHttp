package corp.ny.com.codehttp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import corp.ny.com.codehttp.BuildConfig;
import corp.ny.com.codehttp.R;
import corp.ny.com.codehttp.models.PrepareRequest;
import corp.ny.com.codehttp.system.App;
import corp.ny.com.codehttp.views.ItemListActivity;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 05/06/18.
 */
public class Debugger {

    // Create an explicit intent for an Activity in your app
    private Intent intent;
    private NotificationCompat.Builder notificationBuilder;
    private int notificationId = 1000;
    private NotificationManagerCompat notificationManager;
    private OnRequestExecuteListener listener;
    private List<Long> keys = new ArrayList<>();
    private Map<Long, PrepareRequest> history = new HashMap<>();

    public Debugger(Context context) {
        this.intent = new Intent(context, ItemListActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels();
        }
        initialize(context);
    }

    private void initialize(Context context) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = new NotificationCompat.Builder(context, BuildConfig.LIBRARY_PACKAGE_NAME)
                .setContentTitle("Debugger")
                .setContentText("Make a request to see it")
                .setPriority(Notification.FLAG_HIGH_PRIORITY)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_codehttp);
        notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationChannel createAppNotificationChanel(final int chanelImportance) {
        NotificationChannel channel = new NotificationChannel(BuildConfig.LIBRARY_PACKAGE_NAME, "CodeHttp", chanelImportance);
        channel.setDescription("Log App Http request");
        channel.setShowBadge(true);
        return channel;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannels() {
        final List<NotificationChannel> channels = new ArrayList<>();
        channels.add(createAppNotificationChanel(
                NotificationManagerCompat.IMPORTANCE_HIGH));
        final NotificationManager notificationManager = (NotificationManager)
                App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannels(channels);
        }
    }

    public void addToHistory(PrepareRequest request) {
        long id = Calendar.getInstance().getTime().getTime();
        if (listener != null) listener.run();
        if (keys.isEmpty()) keys.add(id);
        else keys.add(0, id);
        history.put(id, request);
        updateNotification(request);
    }

    public void updateNotification(PrepareRequest request) {
        notificationBuilder.setContentText(request.getRoute());
        notificationBuilder.setSubText(String.format("Status : %s %s", request.getCode(), request.getMessage()));
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public List<Long> getKeys() {
        return keys;
    }

    public void setListener(OnRequestExecuteListener listener) {
        this.listener = listener;
    }

    public Map<Long, PrepareRequest> getHistory() {
        return history;
    }

    public interface OnRequestExecuteListener {
        void run();
    }
}
