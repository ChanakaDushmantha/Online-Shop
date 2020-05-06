package lk.chanaka.dushmantha.groceryonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Itemlist extends AppCompatActivity {

    private TextView name, email;
    private Button btn_logout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        btn_logout = findViewById(R.id.logout);


        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
        //String mToken = user.get(sessionManager.TOKEN);
        //String idd = user.get(sessionManager.SHOPID);

        //System.out.println(idd);

        name.setText(mName);
        email.setText(mEmail);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Toast.makeText(Itemlist.this, "Successfully Logout!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
