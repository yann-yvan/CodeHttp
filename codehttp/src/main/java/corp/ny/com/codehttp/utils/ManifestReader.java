package corp.ny.com.codehttp.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import corp.ny.com.codehttp.system.App;

/**
 * Created by Yann Yvan CEO of N.Y. Corp. on 07/05/18.
 */
public class ManifestReader {
    public static String getMetadataString(String name) {
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return null;
    }

    public static int getMetadataInt(String name) {
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getInt(name, 1);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return 1;
    }

    public static int getMetadataInt(String name, int defValue) {
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getInt(name, defValue);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return defValue;
    }

    public static boolean getMetadataBoolean(String name) {
        try {
            ApplicationInfo appInfo = App.getContext().getPackageManager().getApplicationInfo(
                    App.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getBoolean(name, false);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return false;
    }

}
