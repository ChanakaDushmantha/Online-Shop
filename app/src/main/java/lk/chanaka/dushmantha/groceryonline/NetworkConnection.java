package lk.chanaka.dushmantha.groceryonline;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;

public class NetworkConnection {

    public Context context;

    public NetworkConnection(Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
