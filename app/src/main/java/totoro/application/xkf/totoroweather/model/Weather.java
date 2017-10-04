package totoro.application.xkf.totoroweather.model;

public class Weather {
    private NowWeather nowWeather;
    private DailyForecast dailyForecast;
    private HourlyForecast hourlyForecast;

    public NowWeather getNowWeather() {
        return nowWeather;
    }

    public void setNowWeather(NowWeather nowWeather) {
        this.nowWeather = nowWeather;
    }

    public DailyForecast getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(DailyForecast dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public HourlyForecast getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(HourlyForecast hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }
}
