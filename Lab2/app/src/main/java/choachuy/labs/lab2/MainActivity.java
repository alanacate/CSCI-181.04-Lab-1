package choachuy.labs.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    public static final String REMEMBER_ME_KEY = "rememberMe";
    Button signinButton;
    Button registerButton;
    Button clearButton;
    EditText editUsername;
    EditText editPassword;
    CheckBox checkBox;
    SharedPreferences prefs;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        checkBox = findViewById(R.id.checkBox);
        registerButton = findViewById(R.id.registerButton);
        signinButton = findViewById(R.id.signinButton);
        clearButton = findViewById(R.id.clearButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();
                RealmResults<Users> result = realm.where(Users.class).findAll();
                result = result.sort("username", Sort.ASCENDING);

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                Users user = realm.where(Users.class)
                        .equalTo("username", username)
                        .findFirst();
                Log.d("Login", result.toString());
                if (user != null) { // Check if user exists
                    if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
                        blank();
                    } else if (result.isEmpty()) {
                        nothingSaved();
                    } else if (user.getPassword().equals(password)) {
                        goToLogin();
                    } else {
                        incorrect();
                    }
                } else {
                    noneFound();
                }
                realm.close();
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToClear();
            }
        });
        realm = Realm.getDefaultInstance();
        String savedUuid = prefs.getString("uuid", "");
        if (!savedUuid.isEmpty()) {
            Users user = realm.where(Users.class)
                    .equalTo("uuid", savedUuid)
                    .findFirst();
            if (user != null) {
                loadCredentials(user);
            }
        }
        realm.close();
    }

    public void goToRegister(){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
    public void goToClear(){
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();  

        Toast toast = Toast.makeText(this, "Preferences Cleared", Toast.LENGTH_LONG);
        toast.show();
    }
    public void blank(){
        Toast toast = Toast.makeText(this, "Item/s cannot be blank", Toast.LENGTH_LONG);
        toast.show();
    }
    public void noneFound(){
        Toast toast = Toast.makeText(this, "No User found", Toast.LENGTH_LONG);
        toast.show();
    }
    public void nothingSaved(){
        Toast toast = Toast.makeText(this, "Nothing Saved", Toast.LENGTH_LONG);
        toast.show();
    }
    public void incorrect(){
        Toast toast = Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG);
        toast.show();
    }
    public void goToLogin(){
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean(REMEMBER_ME_KEY, checkBox.isChecked());
        edit.apply();
        String username = editUsername.getText().toString();

        realm = Realm.getDefaultInstance();
        Users user = realm.where(Users.class)
                .equalTo("username", username)
                .findFirst();

        if (user != null) {
            edit.putString("uuid", user.getUuid());
            edit.putBoolean(REMEMBER_ME_KEY, checkBox.isChecked());
        } else {
            noneFound();
        }
        edit.apply();
        realm.close();

        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }
    private void loadCredentials(Users user) {
        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean(REMEMBER_ME_KEY, false);

        if (user != null && rememberMe) {
            editUsername.setText(user.getUsername());
            editPassword.setText(user.getPassword());
            checkBox.setChecked(true);
        } else {
            editUsername.setText("");
            editPassword.setText("");
            checkBox.setChecked(false);
        }
    }
}