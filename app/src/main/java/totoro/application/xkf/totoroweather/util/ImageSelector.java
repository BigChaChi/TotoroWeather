package totoro.application.xkf.totoroweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import totoro.application.xkf.totoroweather.R;

public class ImageSelector {
    public static int selectHeadImage(String weatherCode) {
        //先判断是白天还是晚上
        //再判断天气的类型
        int code = Integer.parseInt(weatherCode);
        int image = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int time = Integer.parseInt(hour);
        boolean isDay = false;
        if (time >= 9 && time <= 21) {
            isDay = true;
        }
        //雾，雨，雪，多云，晴
        if (code == 100) {
            //晴
            image = R.mipmap.header_weather_night_sunny;
            if (isDay) {
                image = R.mipmap.header_weather_day_sunny;
            }
        } else if (code >= 101 && code <= 213) {
            //多云
            image = R.mipmap.header_weather_night_cloudy;
            if (isDay) {
                image = R.mipmap.header_weather_day_cloudy;
            }
        } else if (code >= 300 && code <= 313) {
            //雨
            image = R.mipmap.header_weather_night_rain;
            if (isDay) {
                image = R.mipmap.header_weather_day_rain;
            }
        } else if (code >= 400 && code <= 407) {
            //雪
            image = R.mipmap.header_weather_night_sunny;
            if (isDay) {
                image = R.mipmap.header_weather_day_snow;
            }
        } else if (code >= 500 && code <= 508) {
            //雾
            image = R.mipmap.header_weather_day_fog;
        } else {
            image = R.mipmap.header_image_weather;
        }
        return image;
    }


}
