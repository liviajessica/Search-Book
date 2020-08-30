package id.ac.umn.projectuts_00000013091;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE=0x1;
    EditText user, pass;
    Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean savelogin;
    CheckBox savelogincheckbox;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.txtuser);
        pass = (EditText) findViewById(R.id.txtpass);
        login = (Button) findViewById(R.id.btnlogin);
        sharedPreferences = getSharedPreferences("loginref", MODE_PRIVATE);
        savelogincheckbox = (CheckBox) findViewById(R.id.remember);
        editor = sharedPreferences.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        savelogin = sharedPreferences.getBoolean("savelogin", true);
        if (savelogin) {
            user.setText(sharedPreferences.getString("username", null));
            pass.setText(sharedPreferences.getString("password", null));
        }
    }

    public void login() {
        String usrname = user.getText().toString();
        String passwrd = pass.getText().toString();
        if (usrname.equals("user") && passwrd.equals("useruser")) {
            Toast.makeText(this, "Selamat datang, user!", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
            if (savelogincheckbox.isChecked()) {
                editor.putBoolean("savelogin", true);
                editor.putString("username", usrname);
                editor.putString("password", passwrd);
                editor.commit();
            }
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }
}
