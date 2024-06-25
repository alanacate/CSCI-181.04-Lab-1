package choachuy.labs.lab2;

import static choachuy.labs.lab2.MainActivity.REMEMBER_ME_KEY;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;

public class ThirdActivity extends AppCompatActivity {

    TextView welcomeText;
    Realm realm;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        welcomeText = findViewById(R.id.welcomeText);
        realm = Realm.getDefaultInstance();

        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String savedUuid = prefs.getString("uuid", "");

        Users user = realm.where(Users.class)
                .equalTo("uuid", savedUuid)
                .findFirst();
        displayWelcomeMessage(user);
    }
    private void displayWelcomeMessage(Users user) {
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean(REMEMBER_ME_KEY, false);

        if (rememberMe){
            welcomeText.setText("Welcome " + user.getUsername() + "!!! " + "You will be remembered!");
        }
        else {
            welcomeText.setText("Welcome " + user.getUsername() + "!!!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}
