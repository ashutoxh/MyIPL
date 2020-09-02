package nf.co.myipl.myipl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static ProgressBar progressBar;
    EditText l_name, l_password;
    String password, userId;
    CheckBox showPassword;
    private long back_pressed;
    private Toast exitToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (BackgroundTask.backgroundToast != null)
            BackgroundTask.backgroundToast.cancel();
        if (PostLoginActivity.networkToast != null)
            PostLoginActivity.networkToast.cancel();
        if (GivePredictionBackground.predictionToast != null)
            GivePredictionBackground.predictionToast.cancel();
        if (GivePredictionBackground.oopsToast != null)
            GivePredictionBackground.oopsToast.cancel();
        if (GivePredictionBackground.alreadyToast != null)
            GivePredictionBackground.alreadyToast.cancel();
        if (GivePredictionBackground.notYetActiveToast != null)
            GivePredictionBackground.notYetActiveToast.cancel();

        l_name = findViewById(R.id.eusername);
        l_password = findViewById(R.id.epassword);
        showPassword = findViewById(R.id.showPassword);
        progressBar = findViewById(R.id.progressBar);
        UserInfo userInfo = new UserInfo(this);

        Log.d("SHARED PREF MAIN ", "status: " + userInfo.getSavedState());
        if (userInfo.getSavedState() != null && !userInfo.getSavedState().equals("LOGOUT")) {
            Log.d("MAINNN not logout", "Status " + userInfo.getSavedState()
                    + " Username " + userInfo.getSavedUserName()
                    + " UserId " + userInfo.getSavedUserID());
            Intent intent = new Intent(this, PostLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
//            userInfo.setSAVED_STATE("Alive");
            Log.d("MAINNN else logout", "Status " + userInfo.getSavedUserID() + " Username " + userInfo.getSavedUserName() + " UserId " + userInfo.getSavedUserID());
        }
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void onClickShowPassword(View view) {
        if (!showPassword.isChecked()) {
            l_password.setTransformationMethod(new PasswordTransformationMethod());
            l_password.setSelection(l_password.length());
        } else {
            l_password.setTransformationMethod(null);
            l_password.setSelection(l_password.length());
        }
    }

    public void loguser(View view) {

        String method = "login";
        userId = l_name.getText().toString();
        password = l_password.getText().toString();

        if (userId.isEmpty()) {
            l_name.setError("Username cannot be empty");
            return;
        }
        if (password.isEmpty()) {
            l_password.setError("Password cannot be empty");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, "", userId, password, null);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.finishAffinity();
                exitToast.cancel();
            }
        } else {
            exitToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            exitToast.show();
        }
        back_pressed = System.currentTimeMillis();
    }

}