package totoro.application.xkf.totoroweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.List;

import totoro.application.xkf.totoroweather.R;

public class PreferenceUtil {
    private static Context sContext;

    public static void setContext(Context context) {
        sContext = context;
    }

    private static SharedPreferences getInstance() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static void saveLocalCityId(String id) {
        getInstance().edit().putString(sContext.getString(R.string.localCityId), id).apply();
    }

    public static String getLocalCityId() {
        return getInstance().getString(sContext.getString(R.string.localCityId), null);
    }

    public static void addCity(String name, String id) {
        List<String> list = TextUtil.getIds(getAllCities());
        if (!TextUtil.hasCommonCity(id, list) || list == null) {
            TextUtil.addText(getAllCities(), name + "@" + id);
        }
    }

    public static String getAllCities() {
        return getInstance().getString(sContext.getString(R.string.allCities), "");
    }

    public static boolean getIsNightMode() {
        return getInstance().getBoolean(sContext.getString(R.string.isNightMode), false);
    }

    public static void saveIsNightMode(boolean isNightMode) {
        getInstance().edit().putBoolean(sContext.getString(R.string.isNightMode), isNightMode).apply();
    }
}
