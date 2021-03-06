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
import java.util.Date;


public class FixturesBackground extends AsyncTask<Void, Void, Object[]> {
    @SuppressLint("StaticFieldLeak")
    Context ctx;

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
                SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetDateFormat = new SimpleDateFormat("EEE,MMM d");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayData = jsonArray.getJSONObject(i);
                    String dateOfMatch = arrayData.getString("date").trim();
                    if (!"null".equals(dateOfMatch)) {
                        Date matchDate = originalDateFormat.parse(dateOfMatch);
                        dateArr.add(targetDateFormat.format(matchDate));
                    } else {
                        dateArr.add("TBD");
                    }
                    match1.add(arrayData.getString("match1").trim());
                    match2.add(arrayData.getString("match2").trim());
                    if (!arrayData.has("winner") || arrayData.getString("winner").equalsIgnoreCase("null"))
                        winner.add("--");
                    else
                        winner.add(arrayData.getString("winner"));
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("FixturesBackground : response: " + response + " Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return new Object[]{dateArr, match1, match2, winner};
    }
}
