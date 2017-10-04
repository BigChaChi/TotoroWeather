package totoro.application.xkf.totoroweather.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import totoro.application.xkf.totoroweather.R;
import totoro.application.xkf.totoroweather.application.AppCache;
import totoro.application.xkf.totoroweather.listener.OnLoadFinishListener;
import totoro.application.xkf.totoroweather.model.NowWeather;
import totoro.application.xkf.totoroweather.model.Weather;
import totoro.application.xkf.totoroweather.service.DataService;
import totoro.application.xkf.totoroweather.util.ImageSelector;
import totoro.application.xkf.totoroweather.util.LocationUtil;
import totoro.application.xkf.totoroweather.util.LogUtil;
import totoro.application.xkf.totoroweather.util.NetUtil;
import totoro.application.xkf.totoroweather.util.PreferenceUtil;

public class WeatherActivity extends AppCompatActivity implements AMapLocationListener,
        OnLoadFinishListener, SwipeRefreshLayout.OnRefreshListener {
    private DrawerLayout dlDrawerLayout;
    private CollapsingToolbarLayout ctlCollapsingToolbarLayout;
    private ImageView ivHeaderImage;
    private Toolbar tbToolbar;
    private SwipeRefreshLayout srlRefreshLayout;
    private RecyclerView rvNowWeatherList;
    private RecyclerView rvSuggestionList;
    private RecyclerView rvHourlyList;
    private ImageView ivNowWeatherIcon;
    private TextView tvTmpAndWeather;
    private TextView tvFeelAndWind;

    private Handler mHandler = new Handler();
    private Snackbar mSnackbar;
    private DataService mDataService;

    private static final int REQUEST_CODE = 1;
    private boolean canFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mDataService = AppCache.getDataService();
        PreferenceUtil.setContext(this.getApplicationContext());
        checkPermissions();
    }

    private void checkPermissions() {
        //申请高德所需权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                    PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE);
        } else {
            initViews();
        }
    }

    private void initViews() {
        LocationUtil.locationCity(getApplicationContext(), this);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //加载组件
        dlDrawerLayout = (DrawerLayout) findViewById(R.id.dl_drawer_layout);
        ctlCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_collapsing_layout);
        tbToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        ivHeaderImage = (ImageView) findViewById(R.id.iv_header_image);
        ivNowWeatherIcon = (ImageView) findViewById(R.id.iv_now_weather_icon);
        tvTmpAndWeather = (TextView) findViewById(R.id.tv_tmp_and_weather);
        tvFeelAndWind = (TextView) findViewById(R.id.tv_feel_and_wind);
        srlRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh_layout);
        mSnackbar = Snackbar.make(ctlCollapsingToolbarLayout, "", Snackbar.LENGTH_SHORT);
        srlRefreshLayout.setOnRefreshListener(this);
        //显示菜单按钮
        setSupportActionBar(tbToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        if (NetUtil.isNetConnectivity(this)) {
            //有网就定位当前城市
            srlRefreshLayout.setRefreshing(true);
            LocationUtil.locationCity(getApplicationContext(), this);
        } else {
            mSnackbar.setText("没有网").show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            dlDrawerLayout.openDrawer(Gravity.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dlDrawerLayout.isDrawerOpen(Gravity.START)) {
            dlDrawerLayout.closeDrawers();
        } else if (canFinish) {
            System.exit(0);
        } else {
            //连续点击两次退出的逻辑
            mSnackbar.setText(getString(R.string.finish_prompt));
            mSnackbar.show();
            canFinish = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canFinish = false;
                }
            }, 5000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int value : grantResults) {
                    if (value != PackageManager.PERMISSION_GRANTED) {
                        //不给权限就直接退出
                        System.exit(0);
                    }
                }
                initViews();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                if (mDataService.getCurrentCityId() == null) {
                    LocationUtil.stop();
                    String city = aMapLocation.getCity();
                    ctlCollapsingToolbarLayout.setTitle(city);
                    mDataService.searchCity(city, true, null);
                    mDataService.loadWeatherInfo(city, this);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtil.show("AmapError" + "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void loadSuccess(Weather weather) {
        mSnackbar.setText(getString(R.string.loadSuccess));
        mSnackbar.show();
        //更新一系列天气
        updateNowWeather(weather.getNowWeather());

        srlRefreshLayout.setRefreshing(false);
    }

    public void updateNowWeather(NowWeather nowWeather) {
        String feelAndWnd = "体感温度  " + nowWeather.getFeel() + "°  " + nowWeather.getWindType() + "  " +
                nowWeather.getWindDegree();
        tvFeelAndWind.setText(feelAndWnd);
        String tmpAndWeather = nowWeather.getDescription() + "     " + nowWeather.getTemperature() + "°";
        tvTmpAndWeather.setText(tmpAndWeather);
        ivHeaderImage.setImageResource(ImageSelector.selectHeadImage(nowWeather.getCode()));
        ivNowWeatherIcon.setImageResource(ImageSelector.selectWeatherIcon(nowWeather.getCode()));
    }

    @Override
    public void loadFail() {
        mSnackbar.setText(getString(R.string.loadFail));
        mSnackbar.show();
        srlRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        srlRefreshLayout.setRefreshing(true);
        String city = mDataService.getCurrentCityId();
        if (city == null) {
            LocationUtil.locationCity(this.getApplicationContext(), this);
        } else {
            mDataService.loadWeatherInfo(city, this);
        }
    }


}
