package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONObject;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    static Toast backgroundToast;
    private String loggedInUserName = "", loggedInUserId = "";
    @SuppressLint("StaticFieldLeak")
    private Context ctx;

    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    protected String doInBackground(String... params) {
        String reg_url = Utility.url_host + "/player/register";
        String log_url = Utility.url_host + "/player/login";
        String response_api = "";
        String method = params[0];
        String name = params[1];
        String username = params[2];
        String password = params[3];
        String errorMessage = "";
        String response = null;
        JSONObject postData = new JSONObject();
        if ("register".equals(method)) {
            String contact = params[4];
            String groupName = params[5];
            if (Utility.connection(ctx)) {
                try {
                    Log.v("BGThread(REGISTER) : ", "reg_url : " + reg_url);
                    postData.accumulate("name", name);
                    postData.accumulate("userId", username);
                    postData.accumulate("password", password);
                    postData.accumulate("contactNumber", contact);
                    postData.accumulate("groupName", groupName);
                    Log.v("BGThread(REGISTER) : ", "DATA : " + postData.toString());

                    response = Utility.postData(reg_url, postData.toString());

                    if (response != null) {
                        Log.v("BGThread(REGISTER) : ", "RES DATA : " + response);
                        JSONObject JO = new JSONObject(response);
                        Log.v("BGThread(REGISTER) : ", "RES DATA : " + JO.toString());
                        response_api = JO.getString("action");
                        Log.v("BGThread(REGISTER) : ", "Response : " + response_api);
                        errorMessage = JO.getString("errorMessage");
                        Log.v("BGThread(REGISTER) : ", "Error Message : " + errorMessage);
                    }

                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                    FirebaseCrashlytics.getInstance().log("BackgroundTask(register) : username: " + username + " Exception : " + e.getMessage());
                    e.printStackTrace();
                    Log.v("BGThread(REGISTER) : ", "Error Message : " + e.getMessage());
                }
                Log.v("BGThread(REGISTER) : ", "Error Message : " + response_api);
                switch (response_api) {
                    case "success": {
                        loggedInUserName = name;
                        loggedInUserId = username;
                        return "Registration Successful";
                    }
                    case "failure": {
                        switch (errorMessage) {
                            case "Group Name does not exist.":
                                return "Group name does not exist.";
                            case "Incorrect result size: expected 1, actual 0":
                                return "Username already taken";
                        }
                    }
                    default:
                        return "Some error occurred";
                }
            } else
                return "Please check your Internet Connection";
        } else {
            if (Utility.connection(ctx)) {
                try {
                    postData.accumulate("userId", username);
                    postData.accumulate("password", password);
                    Log.d("BGThread(LOGIN)", "User : " + username + " Pass : " + password);

                    response = Utility.postData(log_url, postData.toString());

                    if (response != null) {
                        Log.v("BGThread(LOGIN) : ", "DATA : " + response);
                        JSONObject JO = new JSONObject(response);
                        Log.v("BGThread(LOGIN) : ", "DATA : " + JO.toString());
                        response_api = JO.getString("action");
                        Log.v("BGThread(LOGIN) : ", "Response : " + response_api);
                        errorMessage = JO.getString("errorMessage");
                        Log.v("BGThread(LOGIN) : ", "Error Message : " + errorMessage);
                        name = JO.getString("name");
                        Log.v("BGThread(LOGIN) : ", "Name : " + name);
                        username = JO.getString("userId");
                        Log.v("BGThread(LOGIN) : ", "UserId : " + username);
                    }
                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                    FirebaseCrashlytics.getInstance().log("BGThread(LOGIN) : username: " + username + " responseData : " + response + " Exception : " + e.getMessage());
                    e.printStackTrace();
                }
                switch (response_api) {
                    case "success": {
                        loggedInUserName = name;
                        loggedInUserId = username;
                        return "Login Successful";
                    }
                    case "failure":
                        switch (errorMessage) {
                            case "Incorrect password":
                                return "Incorrect password";
                            case "Incorrect result size: expected 1, actual 0":
                                return "User doesn't exist";
                        }
                        break;
                    default:
                        return "Some error occurred";
                }
            } else
                return "Please check your Internet Connection";
        }
        return "Some error occurred";
    }

    @Override
    protected void onPostExecute(String result) {

        UserInfo userInfo = new UserInfo(ctx);
        if (MainActivity.progressBar != null)
            MainActivity.progressBar.setVisibility(View.GONE);
        if (Register.progressBar != null)
            Register.progressBar.setVisibility(View.GONE);

        if (result.equals("Login Successful") || result.equals("Registration Successful")) {
            Log.d("BG THREAD", "Result : " + loggedInUserName + " " + loggedInUserId);
            Toast.makeText(ctx, "Welcome " + loggedInUserName, Toast.LENGTH_SHORT).show();
            userInfo.setSAVED_USERNAME(loggedInUserName);
            userInfo.setSAVED_USERID(loggedInUserId);
            Log.d("TEST SHARED STATIC ", " " + userInfo.getSavedUserName());
            Intent intent = new Intent(ctx, PostLoginActivity.class);
            ctx.startActivity(intent);
        } else {
            backgroundToast = Toast.makeText(ctx, result, Toast.LENGTH_SHORT);
            backgroundToast.show();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}