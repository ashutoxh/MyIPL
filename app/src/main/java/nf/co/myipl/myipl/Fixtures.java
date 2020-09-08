package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Fixtures extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    static ListView listView;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;


    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.color_silver), PorterDuff.Mode.SRC_ATOP);

        progressBar.setVisibility(View.VISIBLE);
        FixturesBackground fixturesBackground = new FixturesBackground(this);
        fixturesBackground.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}