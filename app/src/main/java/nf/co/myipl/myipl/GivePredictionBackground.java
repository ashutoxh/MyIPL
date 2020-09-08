package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONObject;

public class GivePredictionBackground extends AsyncTask<String, Void, String> {


    static Toast predictionToast;
    static Toast oopsToast;
    static Toast alreadyToast;
    static Toast notYetActiveToast;
    @SuppressLint("StaticFieldLeak")
    private Context ctx;

    GivePredictionBackground(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String response_api = "";
        String prediction_url = Utility.url_host + "/player/prediction";
        String time;
        String response = null;
        String statusTime = "dead";
        try {
            time = Utility.getTime();

            if (time != null) {
                if (Integer.parseInt(time) < 2) {
                    return "DATA YET TO BE FLUSHED";
                } else if (Integer.parseInt(time) >= 14) {
                    return "dead";
                } else {
                    statusTime = "alive";
                }
                Log.d("GivePredictionBG", "time : " + time);
            } else {
                FirebaseCrashlytics.getInstance().log("GivePredictionBG : User : " + new UserInfo(ctx).getSavedUserID() + "  : time : " + time);
                Log.d("GivePredictionBG", "time : is NULL");
            }
            Log.d("GivePredictionBG", "statusTime : " + statusTime);

            JSONObject postData = new JSONObject();
            Log.d("GivePredictionBG PARAM ", "0 " + params[0] + " 1 " + params[1] + " 2 " + params[2]);
            postData.accumulate("userId", params[0]);
            if (params[1].equals("match1"))
                postData.accumulate("match1", params[2]);
            else
                postData.accumulate("match2", params[2]);

            response = Utility.postData(prediction_url, postData.toString());

            if (response != null) {
                JSONObject JO = new JSONObject(response);
                Log.d("GivePredictionBG", "DATA : " + JO.toString());
                response_api = JO.getString("action");
                Log.d("GivePredictionBG", "Response : " + response_api);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("GivePredictionBG : User : " + new UserInfo(ctx).getSavedUserID() + "  : Response : " + response + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.e("GivePredictionBG", "Error Message : " + e.getMessage());
        }
        switch (response_api) {
            case "success": {
                Bundle bundle = new Bundle();
                bundle.putString("prediction_success", params[2]);
                FirebaseAnalytics.getInstance(ctx).logEvent("prediction_success", bundle);
                return "alive";
            }
            case "failure":
                return "recorded";
            default: {
                FirebaseCrashlytics.getInstance().log("GivePredictionBG : User : " + new UserInfo(ctx).getSavedUserID() + "  : Response : " + response);
                return "Some error occurred";
            }
        }
    }


    @Override
    protected void onPostExecute(String status) {
        PostLoginActivity.progressBar.setVisibility(View.GONE);
        switch (status) {
            case "DATA YET TO BE FLUSHED":
                notYetActiveToast = Toast.makeText(ctx, "Predictions will be enabled from 2:00 AM to 2:00 PM", Toast.LENGTH_SHORT);
                notYetActiveToast.show();
                break;
            case "recorded":
                alreadyToast = Toast.makeText(ctx, "Your prediction for this match is already recorded.", Toast.LENGTH_SHORT);
                alreadyToast.show();
                break;
            case "dead":
                oopsToast = Toast.makeText(ctx, "Oops! Looks like you missed the deadline of 2:00 PM", Toast.LENGTH_SHORT);
                oopsToast.show();
                break;
            case "alive":
                predictionToast = Toast.makeText(ctx, "Your prediction for this match is recorded.", Toast.LENGTH_SHORT);
                predictionToast.show();
                break;
            default:
                Toast.makeText(ctx, status, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
