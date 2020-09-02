package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Leaderboard extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static ListView leaderboard;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);

        leaderboard = findViewById(R.id.leaderboard);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        GetPredNLeaderBackground getPredictionBackground = new GetPredNLeaderBackground(this);
        getPredictionBackground.execute("LEADERBOARD");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

