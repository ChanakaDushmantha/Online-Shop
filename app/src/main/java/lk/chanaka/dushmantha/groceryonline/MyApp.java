package lk.chanaka.dushmantha.groceryonline;

import android.app.Application;
import android.view.View;

import com.androidnetworking.AndroidNetworking;

public class MyApp extends Application {
    String host = "hh";
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
