package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetPredNLeaderBackground extends AsyncTask<String, Void, Object[]> {
    private String method;
    @SuppressLint("StaticFieldLeak")
    private Context ctx;

    GetPredNLeaderBackground(Context ctx) {
        this.ctx = ctx;
    }

    protected Object[] doInBackground(String... params) {
        String prediction_url = Utility.url_host + "/player/predictions/" + new UserInfo(ctx).getSavedUserID();
        String leaderboard_url = Utility.url_host + "/player/leaderboard/" + new UserInfo(ctx).getSavedUserID();
        String response = null;
        method = params[0];
        Log.d("GetPredNLeaderBG", method);

        if ("PREDICTION".equals(method)) {
            String status = getStatusOfTime();
            Log.d("GetPredNLeaderBG(PRED)", method);
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> match1 = new ArrayList<>();
            ArrayList<String> match2 = new ArrayList<>();

            try {
                response = Utility.getData(prediction_url);
                if (response != null) {
                    JSONObject userData = new JSONObject(response);
                    if ("success".equals(userData.getString("action"))) {
                        JSONArray jsonArray = userData.getJSONArray("predictions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject arrayData = jsonArray.getJSONObject(i);
                            name.add(arrayData.getString("userId"));

                            if (arrayData.getString("match1").equalsIgnoreCase("null"))
                                match1.add("--");
                            else
                                match1.add(arrayData.getString("match1"));
                            if (arrayData.getString("match2").equalsIgnoreCase("null"))
                                match2.add("--");
                            else
                                match2.add(arrayData.getString("match2"));
                        }
                        return new Object[]{name, match1, match2, status};
                    }
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("GetPredNLeaderBackground(PREDICTION) : response: " + response + " Exception : " + e.getMessage());
                e.printStackTrace();
            }
        } else if ("LEADERBOARD".equals(method)) {
            Log.d("GetPredNLeaderBG(LEAD)", method);
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> points = new ArrayList<>();
            double point;
            try {
                response = Utility.getData(leaderboard_url);
                if (response != null) {
                    JSONObject userData = new JSONObject(response);
                    if ("success".equals(userData.getString("action"))) {
                        JSONArray jsonArray = userData.getJSONArray("leaderBoardDetails");
                        Log.d("GetPredNLeaderBG(LEAD)", "jsonArray : " + jsonArray);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject arrayData = jsonArray.getJSONObject(i);
                            username.add(arrayData.getString("userId"));
                            point = Double.parseDouble(arrayData.getString("points"));
                            point = (double) Math.round(point * 100) / 100;
                            points.add("" + point);
                        }
                        return new Object[]{username, points};
                    }
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("GetPredNLeaderBackground(LEADERBOARD) : response: " + response + " Exception : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getStatusOfTime() {
        String time = Utility.getTime();
        Log.d("GetPredNLeaderBG", "time : " + time);

        if (time == null || Integer.parseInt(time) < 2) {
            return "dead";
        } else {
            return "alive";
        }
    }

    @Override
    protected void onPostExecute(Object[] temp) {

        if ("PREDICTION".equals(method)) {

            Log.d("GPLB postEXE ", method);
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> match1 = new ArrayList<>();
            ArrayList<String> match2 = new ArrayList<>();
            String status = "";
            if (temp != null) {
                username = (ArrayList<String>) temp[0];
                match1 = (ArrayList<String>) temp[1];
                match2 = (ArrayList<String>) temp[2];
                status = (String) temp[3];
            }
            PredictionAdapter adapter = new PredictionAdapter(ctx, username, match1, match2, status);
            Predictions.listView.setAdapter(adapter);
            Predictions.progressBar.setVisibility(View.INVISIBLE);
        } else if ("LEADERBOARD".equals(method)) {
            Log.d("GPLB postEXE ", method);
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> points = new ArrayList<>();
            if (temp != null) {
                username = (ArrayList<String>) temp[0];
                points = (ArrayList<String>) temp[1];
            }
            LeaderboardAdapter adapter = new LeaderboardAdapter(ctx, username, points);
            Leaderboard.leaderboard.setAdapter(adapter);
            Leaderboard.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
