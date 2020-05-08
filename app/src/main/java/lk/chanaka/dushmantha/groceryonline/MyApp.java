package lk.chanaka.dushmantha.groceryonline;

import android.app.Application;
import android.view.View;

import com.androidnetworking.AndroidNetworking;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
    private String host = "http://10.0.2.2:8000/api"; //should use 10.0.2.2 for local host, laravel 10.0.2.2:8000

    public String getServiceURL() {
        return host;
    }

    public void setServiceURL(String host) {
        this.host = host;
    }
}
