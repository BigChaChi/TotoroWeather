package totoro.application.xkf.totoroweather.json;

import java.util.List;

import totoro.application.xkf.totoroweather.model.City;

public class JsonForCity {
    public List<Info> HeWeather5;

    public class Info {
        public Basic basic;

        public class Basic {
            public String city;
            public String prov;
            public String id;
        }
    }

    public City changeToCity() {
        City city = new City();
        Info info = HeWeather5.get(0);
        city.setId(info.basic.id);
        city.setProvince(info.basic.prov);
        city.setCity(info.basic.city);
        return city;
    }
}
