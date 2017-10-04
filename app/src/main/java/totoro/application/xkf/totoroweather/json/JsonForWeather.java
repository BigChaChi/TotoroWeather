package totoro.application.xkf.totoroweather.json;

import java.util.ArrayList;
import java.util.List;

import totoro.application.xkf.totoroweather.model.DailyForecast;
import totoro.application.xkf.totoroweather.model.HourlyForecast;
import totoro.application.xkf.totoroweather.model.NowWeather;
import totoro.application.xkf.totoroweather.model.Weather;
import totoro.application.xkf.totoroweather.util.LogUtil;

public class JsonForWeather {
    //Json数据的解析
    private List<Info> HeWeather5;

    public class Info {
        public Basic basic;
        public Now now;
        public List<DailyForecast> daily_forecast;
        public List<HourlyForecast> hourly_forecast;
//        public Suggestion suggestion;

        public class Basic {
            public String city;
            public String id;
        }

        public class DailyForecast {
            public Astro astro;

            public class Astro {
                public String sr;
                public String ss;
            }

            public Cond cond;

            public class Cond {
                public String code_d;
                public String txt_d;
                public String txt_n;
            }

            public Tmp tmp;

            public class Tmp {
                public String max;
                public String min;
            }

            public Wind wind;

            public class Wind {
                public String dir;
                public String sc;
            }

        }

        public class HourlyForecast {
            public Cond cond;

            public class Cond {
                public String code;
                public String txt;
            }

            public String date;
            public String tmp;
            public Wind wind;

            public class Wind {
                public String dir;
                public String sc;
            }
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
        DailyForecast dailyForecast = getDailyForecast(info);
        weather.setDailyForecast(dailyForecast);
        HourlyForecast hourlyForecast = getHourlyForecast(info);
        weather.setHourlyForecast(hourlyForecast);
        return weather;
    }

    private HourlyForecast getHourlyForecast(Info info) {
        HourlyForecast hourlyForecast = new HourlyForecast();
        List<HourlyForecast.Item> list = new ArrayList<>();
        for (Info.HourlyForecast hourly : info.hourly_forecast) {
            HourlyForecast.Item item = hourlyForecast.new Item();
            item.setCode(hourly.cond.code);
            item.setTxt(hourly.cond.txt);
            String time = hourly.date.substring(hourly.date.length() - 5);
            item.setTime(time);
            item.setWindDegree(hourly.wind.sc);
            item.setWindType(hourly.wind.dir);
            item.setTemperature(hourly.tmp);
            list.add(item);
        }
        hourlyForecast.setList(list);
        return hourlyForecast;
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

    private DailyForecast getDailyForecast(Info info) {
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast.Item> list = new ArrayList<>();
        for (Info.DailyForecast daily : info.daily_forecast) {
            DailyForecast.Item item = dailyForecast.new Item();
            item.setCodeDay(daily.cond.code_d);
            item.setMaxTemperature(daily.tmp.max);
            item.setMinTemperature(daily.tmp.min);
            item.setSunRise(daily.astro.sr);
            item.setSunSet(daily.astro.ss);
            item.setTxtNight(daily.cond.txt_n);
            item.setTxtDay(daily.cond.txt_d);
            item.setWindDegree(daily.wind.sc);
            item.setWindType(daily.wind.dir);
            list.add(item);
        }
        dailyForecast.setDailyForecast(list);
        return dailyForecast;
    }
}
