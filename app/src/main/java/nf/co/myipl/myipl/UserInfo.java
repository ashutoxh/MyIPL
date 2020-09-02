package nf.co.myipl.myipl;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfo {
    private SharedPreferences sharedPreferences;

    UserInfo(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences("userinfo", 0);
    }


    public void setSAVED_USERID(String SAVED_USERID) {
        sharedPreferences.edit().putString("userId", SAVED_USERID).apply();
    }

    public void setSAVED_USERNAME(String SAVED_USERNAME) {
        sharedPreferences.edit().putString("username", SAVED_USERNAME).apply();
    }

    public void setSAVED_STATE(String SAVED_STATE) {
        sharedPreferences.edit().putString("Status", SAVED_STATE).apply();
    }

    public String getMATCH_COUNT() {
        return sharedPreferences.getString("count", null);
    }

    public void setMATCH_COUNT(int MATCH_COUNT) {
        sharedPreferences.edit().putString("count", "" + MATCH_COUNT).apply();
    }

    public String getMATCH2a() {
        return sharedPreferences.getString("match2a", null);
    }

    public void setMATCH2a(String match2a) {
        sharedPreferences.edit().putString("match2a", match2a).apply();
    }

    public String getMATCH2b() {
        return sharedPreferences.getString("match2b", null);
    }

    public void setMATCH2b(String match2b) {
        sharedPreferences.edit().putString("match2b", match2b).apply();
    }

    public String getSavedUserName() {
        return sharedPreferences.getString("username", null);
    }

    public String getSavedUserID() {
        return sharedPreferences.getString("userId", null);
    }

    public String getSavedState() {
        return sharedPreferences.getString("Status", null);
    }

    public String getMATCH1a() {
        return sharedPreferences.getString("match1a", null);
    }

    public void setMATCH1a(String match1a) {
        sharedPreferences.edit().putString("match1a", match1a).apply();
    }

    public String getMATCH1b() {
        return sharedPreferences.getString("match1b", null);
    }

    public void setMATCH1b(String match1b) {
        sharedPreferences.edit().putString("match1b", match1b).apply();
    }


}
