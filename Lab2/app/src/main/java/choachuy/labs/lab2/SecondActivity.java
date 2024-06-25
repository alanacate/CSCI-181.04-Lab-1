package choachuy.labs.lab2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SecondActivity extends AppCompatActivity {

    Button cancelButton;
    Button saveButton;
    EditText userText;
    EditText passText;
    EditText confirmPassText;
    SharedPreferences prefs;
    Realm realm;

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

        realm = Realm.getDefaultInstance();
        RealmResults<Users> result = realm.where(Users.class).findAll();
        result = result.sort("username", Sort.ASCENDING);

        for (Users i : result)
        {
            Log.d("Register", i.toString());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userText.getText().toString();
                String password = passText.getText().toString();
                String confirmPass = confirmPassText.getText().toString();
                Users existingUser = realm.where(Users.class)
                        .equalTo("username", username)
                        .findFirst();
                if (username == null || username.isEmpty()) {
                    nameBlank();
                } else if ((username == null || username.isEmpty()) || (password == null || password.isEmpty()) || (confirmPass == null || confirmPass.isEmpty())) {
                    blank();
                } else if (!(password == null || password.isEmpty()) && !password.equals(confirmPass)) {
                    mismatch();
                } else if (existingUser != null){
                    exists();
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

    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }

    public void registerAccount(){
        String text = userText.getText().toString();
        String text2 = passText.getText().toString();

        Users newUser =  new Users();
        newUser.setUsername(text);
        newUser.setPassword(text2);

        long count = 0;

        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(newUser);
            realm.commitTransaction();

            count = realm.where(Users.class).count();

            Toast t = Toast.makeText(this, "New User saved. Total: "+count, Toast.LENGTH_LONG);
            t.show();
        }

        catch(Exception e){
            Toast t = Toast.makeText(this, "Error saving", Toast.LENGTH_LONG);
            t.show();
        }

        finish();
    }

    public void exists() {
        Toast toast = Toast.makeText(this, "User already exists", Toast.LENGTH_LONG);
        toast.show();
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