package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class FixturesBackground extends AsyncTask<Void, Void, Object[]> {
    @SuppressLint("StaticFieldLeak")
    Context ctx;
    //char [] time;


    FixturesBackground(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(Object[] objects) {
        ArrayList<String> dateArr = (ArrayList<String>) objects[0];
        ArrayList<String> match1 = (ArrayList<String>) objects[1];
        ArrayList<String> match2 = (ArrayList<String>) objects[2];
        ArrayList<String> winner = (ArrayList<String>) objects[3];
        FixtureAdapter adapter = new FixtureAdapter(ctx, dateArr, match1, match2, winner);
        Fixtures.listView.setAdapter(adapter);
        Fixtures.progressBar.setVisibility(View.GONE);
    }

    @Override
    protected Object[] doInBackground(Void... aVoid) {
        ArrayList<String> dateArr = new ArrayList<>();
        ArrayList<String> match1 = new ArrayList<>();
        ArrayList<String> match2 = new ArrayList<>();
        ArrayList<String> winner = new ArrayList<>();
        String response = null;
        try {
            String schedule_url = Utility.url_host + "/player/scheduler";

            response = Utility.getData(schedule_url);

            if (response != null) {
                JSONObject userData = new JSONObject(response);
                Log.d("FixturesBackground", "response : " + response);
                JSONArray jsonArray = userData.getJSONArray("scheduler");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayData = jsonArray.getJSONObject(i);
//                    dateArr.add(arrayData.getString("date"));
                    Date date1 = new Date(Long.parseLong(arrayData.getString("date")));
                    dateArr.add(simpleDateFormat.format(date1));
                    Log.d("FixturesBackground", "dateArr : " + simpleDateFormat.format(date1));
                    match1.add(arrayData.getString("match1"));
                    match2.add(arrayData.getString("match2"));
                    if (!arrayData.has("winner") || arrayData.getString("winner").equalsIgnoreCase("null"))
                        winner.add("--");
                    else
                        winner.add(arrayData.getString("winner"));
                }
                Log.d("FixturesBackground", "dateArr : " + Arrays.toString(dateArr.toArray()));
                Log.d("FixturesBackground", "match1 : " + Arrays.toString(match1.toArray()));
                Log.d("FixturesBackground", "match2 : " + Arrays.toString(match2.toArray()));
                Log.d("FixturesBackground", "winner : " + Arrays.toString(winner.toArray()));
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("FixturesBackground : response: " + response + " Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return new Object[]{dateArr, match1, match2, winner};
    }
}
