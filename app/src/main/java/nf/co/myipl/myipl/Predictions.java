package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Predictions extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    static ListView listView;
    @SuppressLint("StaticFieldLeak")
    static TextView m2Text, match2a, match2b, match1a, match1b, ngiven1, ngiven2;


    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.color_silver), PorterDuff.Mode.SRC_ATOP);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        m2Text = findViewById(R.id.m2);
        match2a = findViewById(R.id.pText2a);
        match2b = findViewById(R.id.pText2b);
        match1a = findViewById(R.id.pText1a);
        match1b = findViewById(R.id.pText1b);
        ngiven1 = findViewById(R.id.notgiven1);
        ngiven2 = findViewById(R.id.notgiven2);
        UserInfo userInfo = new UserInfo(this);
        Log.d("Predictions", "" + userInfo.getMATCH_COUNT());
        if(null != userInfo.getMATCH_COUNT()) {
            if (userInfo.getMATCH_COUNT().equals("2")) {
                m2Text.setVisibility(View.VISIBLE);
                match1a.setVisibility(View.VISIBLE);
                match1b.setVisibility(View.VISIBLE);
                ngiven1.setVisibility(View.VISIBLE);
            } else {
                m2Text.setVisibility(View.GONE);
                match1a.setVisibility(View.GONE);
                match1b.setVisibility(View.GONE);
                ngiven1.setVisibility(View.GONE);
            }
        }
        GetPredNLeaderBackground getPredictionBackground = new GetPredNLeaderBackground(this);
        getPredictionBackground.execute("PREDICTION");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
