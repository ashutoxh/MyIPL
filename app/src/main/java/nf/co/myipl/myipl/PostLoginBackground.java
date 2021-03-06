package nf.co.myipl.myipl;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

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
        String schedule_url = Utility.url_host + "/player/schedulerForToday";
        ArrayList<String> match1 = new ArrayList<>();
        ArrayList<String> match2 = new ArrayList<>();
        String response = null;

        try {
            response = Utility.getData(schedule_url);

            if (response != null) {
                Log.d("PostLoginBackground", "response : " + response);
                JSONObject userData = new JSONObject(response);
                if ("success".equals(userData.getString("action"))) {
                    JSONArray jsonArray = userData.getJSONArray("scheduler");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayData = jsonArray.getJSONObject(i);
                        match1.add(arrayData.getString("match1"));
                        match2.add(arrayData.getString("match2"));
                    }
                    Log.d("PostLoginBackground", "User : " + new UserInfo(ctx).getSavedUserID() + "  : Response : " + response);
                    FirebaseCrashlytics.getInstance().log("PostLoginBackground(schedule_url) : User : " + new UserInfo(ctx).getSavedUserID() + "  : Response : " + response);
                } else {
                    Log.d("PostLoginBackground", "User : " + new UserInfo(ctx).getSavedUserID() + "  : Response is NULL");
                    FirebaseCrashlytics.getInstance().log("PostLoginBackground(schedule_url) : User : " + new UserInfo(ctx).getSavedUserID() + "  : Response is NULL");
                }
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("PostLoginBackground(schedule_url) : User : " + new UserInfo(ctx).getSavedUserID() + "  : Response : " + response + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.e("PostLoginBackground", "schedule_url : Error Message : " + e.getMessage());
        }
        return new Object[]{match1, match2};
    }

    @Override
    protected void onPostExecute(Object[] temp) {
        if (Utility.connection(ctx)) {
            ArrayList<String> match1;
            ArrayList<String> match2;
            match1 = (ArrayList<String>) temp[0];
            match2 = (ArrayList<String>) temp[1];
            Log.d("PostLoginBackground", " " + match1.toString() + " " + match2.toString());
            ArrayList<String> team = new ArrayList<>();
            if (match1.size() != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PostLoginActivity.textPrediction.setText(LocalDate.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                } else {
                    PostLoginActivity.textPrediction.setText("Today's match(es):");
                }
                Log.d("PostLoginBackground", "match1.size() " + match1.size());
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
                    setMatchIcon(team);
                    new UserInfo(ctx).setMATCH_COUNT(1);
                }
                PostLoginActivity.isMatchToday = true;
                PostLoginActivity.shouldThreadRun = true;
                new PostLoginActivity().refreshTime();
            } else {
                PostLoginActivity.textPrediction.setText("No match today.\nCheck fixtures for more info");
                PostLoginActivity.team1a.setVisibility(View.GONE);
                PostLoginActivity.team1b.setVisibility(View.GONE);
                PostLoginActivity.VS1.setVisibility(View.GONE);
                PostLoginActivity.team2a.setVisibility(View.GONE);
                PostLoginActivity.team2b.setVisibility(View.GONE);
                PostLoginActivity.VS2.setVisibility(View.GONE);
                PostLoginActivity.progressBar.setVisibility(View.GONE);
                PostLoginActivity.timeLeft.setVisibility(View.GONE);
                PostLoginActivity.isMatchToday = false;
                PostLoginActivity.shouldThreadRun = false;
            }
            PostLoginActivity.predictionbtn.setClickable(true);
            PostLoginActivity.leaderboardbtn.setClickable(true);
            PostLoginActivity.progressBar.setVisibility(View.GONE);
        } else {
            PostLoginActivity.textPrediction.setText("Please check your Internet Connection.\nPull down to refresh");
            PostLoginActivity.team1a.setVisibility(View.GONE);
            PostLoginActivity.team1b.setVisibility(View.GONE);
            PostLoginActivity.VS1.setVisibility(View.GONE);
            PostLoginActivity.team2a.setVisibility(View.GONE);
            PostLoginActivity.team2b.setVisibility(View.GONE);
            PostLoginActivity.VS2.setVisibility(View.GONE);
            PostLoginActivity.progressBar.setVisibility(View.GONE);
            PostLoginActivity.timeLeft.setVisibility(View.GONE);
            PostLoginActivity.predictionbtn.setClickable(false);
            PostLoginActivity.leaderboardbtn.setClickable(false);
            PostLoginActivity.shouldThreadRun = false;
        }
    }

    private void setMatchIcon(ArrayList<String> team) {

        Button[] btn = {PostLoginActivity.team2a, PostLoginActivity.team2b, PostLoginActivity.team1a, PostLoginActivity.team1b};
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i) != null) {
                Log.d("PostLoginBackground", "team.get(i) " + team.get(i));
                switch (team.get(i).trim()) {
                    case "RCB": {
                        btn[i].setBackgroundResource(R.mipmap.rcb);
                        btn[i].setText(team.get(i));
                        btn[i].setTextSize(0);
                        break;
                    }
                    case "MI": {
                        btn[i].setBackgroundResource(R.mipmap.mi);
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
