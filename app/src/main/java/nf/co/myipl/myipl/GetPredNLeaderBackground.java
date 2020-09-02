package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetPredNLeaderBackground extends AsyncTask<String, Void, Object[]> {
    private String method;
    @SuppressLint("StaticFieldLeak")
    private Context ctx;
    private char[] time;

    GetPredNLeaderBackground(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(Object[] temp) {

        if (method.equals("PREDICTION")) {

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
        } else if (method.equals("LEADERBOARD")) {
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

    protected Object[] doInBackground(String... params) {
        String line = "";
        HttpURLConnection httpURLConnection;
        String prediction_url = "https://myipl1-235507.appspot.com/player/predictions/" + new UserInfo(ctx).getSavedUserID();
        String leaderboard_url = "https://myipl1-235507.appspot.com/player/leaderboard/" + new UserInfo(ctx).getSavedUserID();
        Log.d("LEADERBOARD URL ", leaderboard_url);
        String response_api = "";
        StringBuilder data = new StringBuilder();
        method = params[0];
        Log.d("GPLB method? ", method);

        if (method.equals("PREDICTION")) {
            String status = getTime();
            Log.d("GPLB doInB ", method);
            ArrayList<String> name = new ArrayList<>();
            ArrayList<String> match1 = new ArrayList<>();
            ArrayList<String> match2 = new ArrayList<>();

            try {
                httpURLConnection = (HttpURLConnection) new URL(prediction_url).openConnection();

                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json; charset=UTF-8");
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }
//                data = "{\"action\":\"success\"," + "\"predictions\":" + "["
//                        + "{\"userId\":\"ashutoxh\"," + "\"match1\":null," + "\"match2\":\"RCB\"},"
//                        + "{\"userId\":\"skawes\"," + "\"match1\":MI," + "\"match2\":\"RCB\"},"
//                        + "{\"userId\":\"saurabh727\"," + "\"match1\":KKR," + "\"match2\":null},"
//                        + "{\"userId\":\"jigar181\"," + "\"match1\":MI," + "\"match2\":\"RCB\"}"
//                        + "]" + "}";
//                Log.d("ADAPTER DATA ", data.toString());
                JSONObject userData = new JSONObject(data.toString());
                JSONArray jsonArray = userData.getJSONArray("predictions");
//                Log.d("JSONARRAYYY ", "" + jsonArray);
                response_api = userData.getString("action");
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

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("GetPredNLeaderBackground(PREDICTION) : data: " + data.toString() + " Exception : " + e.getMessage());
                e.printStackTrace();
            }
            if (response_api.equals("success")) {
                return new Object[]{name, match1, match2, status};
            }
        } else if (method.equals("LEADERBOARD")) {
            Log.d("GPLB doInB ", method);
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> points = new ArrayList<>();
            double point;
            try {
                httpURLConnection = (HttpURLConnection) new URL(leaderboard_url).openConnection();
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json; charset=UTF-8");

                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }
//                data = "{\"action\":\"success\"," + "\"leaderboard\":" + "["
//                        + "{\"userId\":\"ashutoxh\"," + "\"points\":100.25},"
//                        + "{\"userId\":\"skawes\"," + "\"points\":100},"
//                        + "{\"userId\":\"saurabh727\"," + "\"points\":100},"
//                        + "{\"userId\":\"jigar181\"," + "\"points\":100}"
//                        + "]" + "}";
//                Log.d("ADAPTER DATA ", data.toString());

                JSONObject userData = new JSONObject(data.toString());
                JSONArray jsonArray = userData.getJSONArray("leaderBoardDetails");
                Log.d("JSONARRAYYY ", "" + jsonArray);
                response_api = userData.getString("action");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayData = jsonArray.getJSONObject(i);
                    username.add(arrayData.getString("userId"));
                    point = Double.parseDouble(arrayData.getString("points"));
                    point = (double) Math.round(point * 100) / 100;
                    points.add("" + point);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("GetPredNLeaderBackground(LEADERBOARD) : data: " + data.toString() + " Exception : " + e.getMessage());
                e.printStackTrace();
            }
            if (response_api.equals("success")) {
                return new Object[]{username, points};
            }

        }
        return null;
    }

    private String getTime() {
        String dateTime;
        StringBuilder data = new StringBuilder();
        try {
            URL url = new URL("/health-check");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
            Log.v("GetPredNLeaderBackgroun",data.toString());
            JSONObject JO = new JSONObject(data.toString());
            dateTime = JO.getString("formatted");
//          dateTime = "2018-04-14 00:00:00";
            time = dateTime.substring(11, 19).toCharArray();
            Log.d("ARRAYYYYYYYYY : ", "Value : " + Integer.parseInt(("" + time[0]) + ("" + time[1])));
            httpURLConnection.disconnect();
            inputStream.close();
            bufferedReader.close();
            if (Integer.parseInt(("" + time[0]) + ("" + time[1])) < 2) {
                return "dead";
            } else {
                return "alive";
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("GetPredNLeaderBackground(getTime()) : data: " + data.toString() + " Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return "dead";
    }
}
