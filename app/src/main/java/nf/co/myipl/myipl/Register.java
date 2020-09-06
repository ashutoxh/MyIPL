package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;
    EditText e_name, e_password, e_confirmPassword, e_contact, e_username, e_groupName;
    String name, password, confirmPassword, contact, username, groupName;
    CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e_name = findViewById(R.id.name);
        e_password = findViewById(R.id.password);
        e_confirmPassword = findViewById(R.id.confirmpassword);
        e_contact = findViewById(R.id.contact);
        e_username = findViewById(R.id.rank);
        progressBar = findViewById(R.id.progressBar);
        showPassword = findViewById(R.id.showPassword);
        e_groupName = findViewById(R.id.groupName);
        e_name.setText(null);
        e_password.setText(null);
        e_contact.setText(null);
        e_username.setText(null);
    }

    public void onClickShowPassword(View view) {
        if (!showPassword.isChecked()) {
            e_password.setTransformationMethod(new PasswordTransformationMethod());
            e_confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            e_password.setTransformationMethod(null);
            e_confirmPassword.setTransformationMethod(null);
        }
    }

    public void reguser(View view) {


        name = e_name.getText().toString().toUpperCase();
        password = e_password.getText().toString();
        confirmPassword = e_confirmPassword.getText().toString();
        contact = e_contact.getText().toString().toUpperCase();
        username = e_username.getText().toString();
        groupName = e_groupName.getText().toString();

        String method = "register";

        if (name.isEmpty() || !name.matches("[a-z A-Z]+")) {
            e_name.setError("Name cannot be empty");
            return;
        }
        if (name.length() > 30) {
            e_name.setError("Name too long");
            return;
        }
        if (username.length() < 3 || username.length() > 12) {
            e_username.setError("Username must be 3 to 12 character long");
            return;
        }
        if (!username.matches("[0-9a-zA-z_]+")) {
            e_username.setError("Only alphabets, numbers and _ are allowed ");
            return;
        }
        if (password.length() < 6) {
            e_password.setError("Password must be at least 6 characters long");
            return;
        }
        if (password.length() > 30) {
            e_password.setError("Password too long");
            return;
        }
        if (!password.equals(confirmPassword)) {
            e_confirmPassword.setError("Password doesn't match");
            return;
        }
        if (!contact.matches("[0-9]+") || contact.length() > 10 || contact.length() < 8) {
            e_contact.setError("Contact must be valid 8 to 10 digit");
            return;
        }
        if (groupName.isEmpty()) {
            e_groupName.setError("Please enter group name provided");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, name, username, password, contact, groupName);
    }

}

