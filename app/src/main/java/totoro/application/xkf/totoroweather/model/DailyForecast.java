package totoro.application.xkf.totoroweather.model;

import java.util.List;

public class DailyForecast {
    //未来几天的预测
    private List<Item> dailyForecast;

    public class Item {
        //日出时间
        private String sunRise;
        //日落时间
        private String sunSet;
        //白天的天气代码
        private String codeDay;
        //白天天气描述
        private String txtDay;
        private String textNight;
        private String maxTemperature;
        private String minTemperature;
    }
}
