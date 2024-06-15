package choachuy.labs.lab1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    Button cancelButton;
    Button saveButton;
    EditText userText;
    EditText passText;
    EditText confirmPassText;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        userText = findViewById(R.id.userText);
        passText = findViewById(R.id.passText);
        confirmPassText = findViewById(R.id.confirmPassText);

        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userText.getText().toString();
                String password = passText.getText().toString();
                String confirmPass = confirmPassText.getText().toString();
                if (username == null || username.isEmpty()) {
                    nameBlank();
                } else if ((username == null || username.isEmpty()) || (password == null || password.isEmpty()) || (confirmPass == null || confirmPass.isEmpty())) {
                    blank();
                } else if (!(password == null || password.isEmpty()) && !password.equals(confirmPass)) {
                    mismatch();
                } else {
                    registerAccount();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void registerAccount(){
        String text = userText.getText().toString();
        String text2 = passText.getText().toString();

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("user", text);
        edit.putString("pass", text2);
        edit.apply();

        Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_LONG);
        toast.show();

        finish();

    }
    public void goBack(){
        finish();
    }
    public void blank(){
        Toast toast = Toast.makeText(this, "Item/s cannot be blank", Toast.LENGTH_LONG);
        toast.show();
    }
    public void nameBlank(){
        Toast toast = Toast.makeText(this, "Name must not be blank", Toast.LENGTH_LONG);
        toast.show();
    }
    public void mismatch(){
        Toast toast = Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_LONG);
        toast.show();
    }

}