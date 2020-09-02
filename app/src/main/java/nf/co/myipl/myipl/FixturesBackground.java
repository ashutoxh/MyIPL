package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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
        StringBuilder data = new StringBuilder();
        try {
            String schedule_url = "https://myipl1-235507.appspot.com/player/scheduler";
            HttpURLConnection httpURLConnection;
            String line = "";
            httpURLConnection = (HttpURLConnection) new URL(schedule_url).openConnection();

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
//            data.append("{\"action\":\"success\"," + "\"date\":" + "["
//                    + "{\"date\":\"2018-04-07\"," + "\"match1\":\"MI\"," + "\"match2\":\"RCB\"},"
//                    + "{\"date\":\"2018-04-07\"," + "\"match1\":\"RR\"," + "\"match2\":\"SRH\"},"
//                    + "{\"date\":\"2018-04-08\"," + "\"match1\":\"CSK\"," + "\"match2\":\"RCB\"}"
//                    + "]" + "}");
//            Log.d("Get Schedule ", data.toString());

            JSONObject userData = new JSONObject(data.toString());
            JSONArray jsonArray = userData.getJSONArray("scheduler");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayData = jsonArray.getJSONObject(i);
                dateArr.add(arrayData.getString("date"));
                match1.add(arrayData.getString("match1"));
                match2.add(arrayData.getString("match2"));
                if (arrayData.getString("winner").equalsIgnoreCase("null"))
                    winner.add("--");
                else
                    winner.add(arrayData.getString("winner"));
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("FixturesBackground : data: " + data.toString() + " Exception : " + e.getMessage());
            e.printStackTrace();
        }
        return new Object[]{dateArr, match1, match2, winner};
    }
}
