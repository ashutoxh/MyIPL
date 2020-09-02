package nf.co.myipl.myipl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;


public class PostLoginBackground extends AsyncTask<String, Void, Object[]> {

    private Context ctx;

    PostLoginBackground(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected Object[] doInBackground(String... strings) {
        {
            String KEY = "KXHDJMF1OPW7";
            String dateTime;
            String date = "";
            ArrayList<String> match1 = new ArrayList<>();
            ArrayList<String> match2 = new ArrayList<>();
            try {
                URL url = new URL("");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder data = new StringBuilder();
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }
                JSONObject JO = new JSONObject(data.toString());
                dateTime = JO.getString("formatted");
//                dateTime = "2018-05-21 04:00:00";
                date = dateTime.substring(0, 10);
//            Log.d("Get Schedule DATE ", "" + date);
                httpURLConnection.disconnect();
                inputStream.close();
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String schedule_url = "https://myipl1-235507.appspot.com/player/scheduler";
            HttpURLConnection httpURLConnection;
            StringBuilder data = new StringBuilder();
            String line = "";
            try {
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
//                Log.d("JSONARRAYYY ", "" + jsonArray);
                int count = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject arrayData = jsonArray.getJSONObject(i);
                    if (date.equals(arrayData.getString("date"))) {
                        match1.add(arrayData.getString("match1"));
                        match2.add(arrayData.getString("match2"));
                        count++;
                    }
                    if (count > 2)
                        break;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Object[]{date, match1, match2};
        }
    }

    @Override
    protected void onPostExecute(Object[] temp) {
        if (Utility.connection(ctx)) {
            ArrayList<String> match1 = new ArrayList<>();
            ArrayList<String> match2 = new ArrayList<>();
            match1.add("MI");
            match1.add("RCB");
            match2.add("CSK");
            match2.add("DC");
//        match1 = (ArrayList<String>) temp[1];
//        match2 = (ArrayList<String>) temp[2];
            //Log.d("POSTLOGIN OBJECT ", " " + match1.toString() + " " + match2.toString());
            ArrayList<String> team = new ArrayList<>();
            if (match1.size() != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PostLoginActivity.textPrediction.setText(LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                } else {
                    PostLoginActivity.textPrediction.setText("Today's match(es):");
                }
                if (match1.size() > 1) {
                    PostLoginActivity.team1a.setVisibility(View.VISIBLE);
                    PostLoginActivity.team1b.setVisibility(View.VISIBLE);
                    PostLoginActivity.VS1.setVisibility(View.VISIBLE);
                    PostLoginActivity.team2a.setVisibility(View.VISIBLE);
                    PostLoginActivity.team2b.setVisibility(View.VISIBLE);
                    PostLoginActivity.VS2.setVisibility(View.VISIBLE);
                    team.add(match1.get(0));
                    team.add(match2.get(0));
                    team.add(match1.get(1));
                    team.add(match2.get(1));
                    new UserInfo(ctx).setMATCH2a(team.get(0));
                    new UserInfo(ctx).setMATCH2b(team.get(1));
                    new UserInfo(ctx).setMATCH1a(team.get(2));
                    new UserInfo(ctx).setMATCH1b(team.get(3));
                    //Log.d("POSTLOGIN COUNT MORE 1 ", " " + team.toString());
                    setMatchIcon(team);
                    new UserInfo(ctx).setMATCH_COUNT(2);
                } else {
                    PostLoginActivity.team2a.setVisibility(View.VISIBLE);
                    PostLoginActivity.team2b.setVisibility(View.VISIBLE);
                    PostLoginActivity.VS1.setVisibility(View.VISIBLE);
                    team.add(match1.get(0));
                    team.add(match2.get(0));
                    team.add(null);
                    team.add(null);
                    new UserInfo(ctx).setMATCH2a(team.get(0));
                    new UserInfo(ctx).setMATCH2b(team.get(1));

                    //Log.d("POSTLOGIN COUNT LESS 1 ", " " + team.toString());
                    setMatchIcon(team);
                    new UserInfo(ctx).setMATCH_COUNT(1);
                }
            } else {
                PostLoginActivity.textPrediction.setText("No match today. Check fixtures for more info.");
                PostLoginActivity.team1a.setVisibility(View.GONE);
                PostLoginActivity.team1b.setVisibility(View.GONE);
                PostLoginActivity.VS1.setVisibility(View.GONE);
                PostLoginActivity.team2a.setVisibility(View.GONE);
                PostLoginActivity.team2b.setVisibility(View.GONE);
                PostLoginActivity.VS2.setVisibility(View.GONE);
                PostLoginActivity.progressBar.setVisibility(View.GONE);
            }
            PostLoginActivity.leaderboardbtn.setClickable(true);
            PostLoginActivity.predictionbtn.setClickable(true);
            PostLoginActivity.progressBar.setVisibility(View.GONE);
        } else {
            PostLoginActivity.textPrediction.setText("Please check your Internet Connection and restart the app");
            PostLoginActivity.team1a.setVisibility(View.GONE);
            PostLoginActivity.team1b.setVisibility(View.GONE);
            PostLoginActivity.VS1.setVisibility(View.GONE);
            PostLoginActivity.team2a.setVisibility(View.GONE);
            PostLoginActivity.team2b.setVisibility(View.GONE);
            PostLoginActivity.VS2.setVisibility(View.GONE);
            PostLoginActivity.progressBar.setVisibility(View.GONE);
            PostLoginActivity.timeLeft.setVisibility(View.GONE);
        }
        PostLoginActivity.refreshTime();
    }

    private void setMatchIcon(ArrayList<String> team) {

        Button[] btn = {PostLoginActivity.team2a, PostLoginActivity.team2b, PostLoginActivity.team1a, PostLoginActivity.team1b};
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i) != null) {
                switch (team.get(i)) {
                    case "MI": {
                        btn[i].setBackgroundResource(R.mipmap.mi);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "RCB": {
                        btn[i].setBackgroundResource(R.mipmap.rcb);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "KKR": {
                        btn[i].setBackgroundResource(R.mipmap.kkr);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "CSK": {
                        btn[i].setBackgroundResource(R.mipmap.csk);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "RR": {
                        btn[i].setBackgroundResource(R.mipmap.rr);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "SRH": {
                        btn[i].setBackgroundResource(R.mipmap.srh);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "DC": {
                        btn[i].setBackgroundResource(R.mipmap.dc);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "KXIP": {
                        btn[i].setBackgroundResource(R.mipmap.kxip);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + team.get(i));
                }
            } else {
                PostLoginActivity.team1a.setVisibility(View.GONE);
                PostLoginActivity.team1b.setVisibility(View.GONE);
                PostLoginActivity.VS1.setVisibility(View.GONE);
                PostLoginActivity.timeLeft.setVisibility(View.GONE);
                break;
            }
        }
    }
}
