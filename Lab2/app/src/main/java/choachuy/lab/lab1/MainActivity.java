package choachuy.lab.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {
    private static final String REMEMBER_ME_KEY = "rememberMe";
    Button signinButton;
    Button registerButton;
    Button clearButton;
    EditText editUsername;
    EditText editPassword;
    CheckBox checkBox;
    SharedPreferences prefs;

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
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String s = prefs.getString("user", null);
                String s2 = prefs.getString("pass", null);

                if ((username == null || username.isEmpty()) || (password == null || password.isEmpty())) {
                    blank();
                } else if ((s == null || s.isEmpty()) || (s2 == null || s2.isEmpty())) {
                    nothingSaved();
                } else if((!username.equals(s)) || (!password.equals(s2))) {
                    incorrect();
                } else {
                    goToLogin();
                }
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToClear();
            }
        });
        loadCredentials();
    }
    public void goToRegister(){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
    public void goToClear(){
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();

        Toast toast = Toast.makeText(this, "User Cleared", Toast.LENGTH_LONG);
        toast.show();
    }
    public void blank(){
        Toast toast = Toast.makeText(this, "Item/s cannot be blank", Toast.LENGTH_LONG);
        toast.show();
    }
    public void nothingSaved(){
        Toast toast = Toast.makeText(this, "Nothing Saved", Toast.LENGTH_LONG);
        toast.show();
    }
    public void incorrect(){
        Toast toast = Toast.makeText(this, "Incorrect Credentials", Toast.LENGTH_LONG);
        toast.show();
    }
    public void goToLogin(){
        String text = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        SharedPreferences.Editor edit = prefs.edit();
        if (checkBox.isChecked()) {
            edit.putString("user", text);
            edit.putString("pass", password);
        }
        edit.putBoolean(REMEMBER_ME_KEY, checkBox.isChecked());
        edit.apply();

        Intent intent = new Intent(this, ThirdActivity.class);
        if (checkBox.isChecked()) {
            intent.putExtra("rememberMe", "Welcome "+ text +"!!! You will be remembered!!!");
        } else {
            intent.putExtra("rememberMe", "Welcome "+ text +"!!!");
        }
        startActivity(intent);
    }
    private void loadCredentials() {
        boolean rememberMe = prefs.getBoolean(REMEMBER_ME_KEY, false);
        if (rememberMe) {
            String savedUsername = prefs.getString("user", "");
            String savedPassword = prefs.getString("pass", "");
            editUsername.setText(savedUsername);
            editPassword.setText(savedPassword);
            checkBox.setChecked(true);
        } else {
            editUsername.setText("");
            editPassword.setText("");
            checkBox.setChecked(false);
        }
    }
}