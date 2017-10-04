package totoro.application.xkf.totoroweather.json;

import java.util.List;

import totoro.application.xkf.totoroweather.model.NowWeather;
import totoro.application.xkf.totoroweather.model.Weather;
import totoro.application.xkf.totoroweather.util.LogUtil;

public class JsonForWeather {
    //Json数据的解析
    private List<Info> HeWeather5;

    public class Info {
        public Basic basic;
        public Now now;
//        public DailyForecast daily_forecast;
//        public HourlyForecast hourly_forecast;
//        public Suggestion suggestion;

        public class Basic {
            public String city;
            public String id;
        }

        public class DailyForecast {
        }

        public class HourlyForecast {
        }

        public class Now {
            public Cond cond;

            public class Cond {
                public String code;
                public String txt;
            }

            public String fl;
            public String tmp;
            public Wind wind;

            public class Wind {
                public String dir;
                public String sc;
            }
        }

        public class Suggestion {
        }
    }

    public Weather changeToWeather() {
        Weather weather = new Weather();
        Info info = HeWeather5.get(0);
        NowWeather nowWeather = getNowWeather(info);
        weather.setNowWeather(nowWeather);
        return weather;
    }

    private NowWeather getNowWeather(Info info) {
        NowWeather nowWeather = new NowWeather();
        nowWeather.setName(info.basic.city);
        nowWeather.setId(info.basic.id);
        nowWeather.setCode(info.now.cond.code);
        nowWeather.setDescription(info.now.cond.txt);
        nowWeather.setFeel(info.now.fl);
        nowWeather.setTemperature(info.now.tmp);
        nowWeather.setWindDegree(info.now.wind.sc);
        nowWeather.setWindType(info.now.wind.dir);
        return nowWeather;
    }
}
