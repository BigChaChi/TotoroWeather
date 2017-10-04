package totoro.application.xkf.totoroweather.listener;


import totoro.application.xkf.totoroweather.model.City;

public interface OnSearchFinishListener {
    void onSearchSuccess(City city);

    void onSearchFail();
}
