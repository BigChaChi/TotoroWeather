package totoro.application.xkf.totoroweather.util;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationUtil {
    private static AMapLocationClient client;

    public static void locationCity(Context appContext, AMapLocationListener listener) {
        client = new AMapLocationClient(appContext);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        option.setOnceLocationLatest(true);
        client.setLocationOption(option);
        client.startLocation();
        client.setLocationListener(listener);
    }

    public static void stop() {
        client.stopLocation();
        client.onDestroy();
    }
}
