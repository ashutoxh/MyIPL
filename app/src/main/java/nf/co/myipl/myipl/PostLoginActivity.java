package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoUnit.MINUTES;

public class PostLoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    static Button team1a, team1b, team2a, team2b, leaderboardbtn, predictionbtn;
    @SuppressLint("StaticFieldLeak")
    static TextView VS1, VS2, textPrediction, timeLeft;
    static Toast exitToast, networkToast;
    static boolean isMatchToday = false;
    static boolean shouldThreadRun = true;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progressBar;
    String userName = "", userID = "";
    Context ctx;
    UserInfo userInfo;
    private long back_pressed;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlogin);
        userInfo = new UserInfo(this);
        userName = userInfo.getSavedUserName();
        userID = userInfo.getSavedUserID();
        userInfo.setSAVED_STATE("Alive");
        Log.d("POSTLOGIN", userName + " " + userID);
        Log.d("POSTLOGIN", "status: " + userInfo.getSavedState());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My IPL");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView loggedInUserName = header.findViewById(R.id.nav_header_name);
        TextView getPoints = header.findViewById(R.id.nav_header_points);
        loggedInUserName.setText(userName);
        getPoints.setText(userID);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        team1a = findViewById(R.id.team1a);
        team1b = findViewById(R.id.team1b);
        team2a = findViewById(R.id.team2a);
        team2b = findViewById(R.id.team2b);
        leaderboardbtn = findViewById(R.id.btn_leaderboard);
        predictionbtn = findViewById(R.id.btn_prediction);
        VS1 = findViewById(R.id.VS1);
        VS2 = findViewById(R.id.VS2);
        textPrediction = findViewById(R.id.textPrediction);
        timeLeft = findViewById(R.id.timeLeft);
        progressBar = findViewById(R.id.progressBar);
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + userID);
        team2a.setVisibility(View.GONE);
        team2b.setVisibility(View.GONE);
        team1a.setVisibility(View.GONE);
        team1b.setVisibility(View.GONE);
        VS1.setVisibility(View.GONE);
        VS2.setVisibility(View.GONE);
        timeLeft.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        leaderboardbtn.setClickable(false);
        predictionbtn.setClickable(false);
        PostLoginBackground postLoginBackground = new PostLoginBackground(this);
        postLoginBackground.execute();

        swipeRefreshLayout.setOnRefreshListener(this::recreate);
    }

    public void givePrediction2a(View view) {
        ctx = this;
        Log.d("POSTLOGIN", "givePrediction2a " + team2a.getText().toString());
        runOnUiThread(() -> {
            if (Utility.connection(ctx)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Prediction");
                builder.setMessage("Are you sure you want to choose " + team2a.getText().toString().toUpperCase() + "?");
                builder.setIcon(R.mipmap.mainicon);
                builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (BackgroundTask.backgroundToast != null)
                        BackgroundTask.backgroundToast.cancel();
                    GivePredictionBackground process = new GivePredictionBackground(ctx);
                    process.execute(userID, "match1", team2a.getText().toString());
                    dialog.dismiss();
                });
                builder.setNegativeButton("No",
                        (DialogInterface dialog, int id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                networkToast = Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT);
                networkToast.show();
            }
        });
    }

    public void givePrediction2b(View view) {
        ctx = this;
        Log.d("POSTLOGIN", "givePrediction2b " + team2b.getText().toString());
        runOnUiThread(() -> {
            if (Utility.connection(ctx)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Prediction");
                builder.setMessage("Are you sure you want to choose " + team2b.getText().toString().toUpperCase() + "?");
                builder.setIcon(R.mipmap.mainicon);
                builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (BackgroundTask.backgroundToast != null)
                        BackgroundTask.backgroundToast.cancel();
                    GivePredictionBackground process = new GivePredictionBackground(ctx);
                    process.execute(userID, "match1", team2b.getText().toString());
                    dialog.dismiss();
                });
                builder.setNegativeButton("No",
                        (DialogInterface dialog, int id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                networkToast = Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT);
                networkToast.show();
            }
        });
    }

    public void givePrediction1a(View view) {
        ctx = this;
        Log.d("POSTLOGIN", "givePrediction1a " + team1a.getText().toString());
        runOnUiThread(() -> {
            if (Utility.connection(ctx)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Prediction");
                builder.setMessage("Are you sure you want to choose " + team1a.getText().toString().toUpperCase() + "?");
                builder.setIcon(R.mipmap.mainicon);
                builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (BackgroundTask.backgroundToast != null)
                        BackgroundTask.backgroundToast.cancel();
                    GivePredictionBackground process = new GivePredictionBackground(ctx);
                    process.execute(userID, "match2", team1a.getText().toString());
                    dialog.dismiss();
                });
                builder.setNegativeButton("No",
                        (DialogInterface dialog, int id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                networkToast = Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT);
                networkToast.show();
            }
        });
    }

    public void givePrediction1b(View view) {
        ctx = this;
        Log.d("POSTLOGIN", "givePrediction1b " + team1b.getText().toString());
        runOnUiThread(() -> {
            if (Utility.connection(ctx)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Prediction");
                builder.setMessage("Are you sure you want to choose " + team1b.getText().toString().toUpperCase() + "?");
                builder.setIcon(R.mipmap.mainicon);
                builder.setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (BackgroundTask.backgroundToast != null)
                        BackgroundTask.backgroundToast.cancel();
                    GivePredictionBackground process = new GivePredictionBackground(ctx);
                    process.execute(userID, "match2", team1b.getText().toString());
                    dialog.dismiss();
                });
                builder.setNegativeButton("No",
                        (DialogInterface dialog, int id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                networkToast = Toast.makeText(ctx, "Please check your Internet Connection", Toast.LENGTH_SHORT);
                networkToast.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BackgroundTask.backgroundToast != null)
            BackgroundTask.backgroundToast.cancel();
        if (networkToast != null)
            networkToast.cancel();
        if (GivePredictionBackground.predictionToast != null)
            GivePredictionBackground.predictionToast.cancel();
        if (GivePredictionBackground.oopsToast != null)
            GivePredictionBackground.oopsToast.cancel();
        if (GivePredictionBackground.alreadyToast != null)
            GivePredictionBackground.alreadyToast.cancel();
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.finishAffinity();
                exitToast.cancel();
            }
        } else {
            exitToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            exitToast.show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            ctx = this;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Are you sure you want Logout?");
            builder.setIcon(R.mipmap.mainicon);
            builder.setPositiveButton("Yes", (DialogInterface dialog, int id1) -> {
                Log.d("LOGOUTTTT ", "Username " + userName + " UserId " + userID);
                Log.d("LOGOUTTT SHARED ", "status: " + userInfo.getSavedState());
                userInfo = new UserInfo(ctx);
                userInfo.setSAVED_STATE(null);
                userInfo.setSAVED_USERID(null);
                userInfo.setSAVED_USERNAME(null);
                Intent intent = new Intent(ctx, MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            });
            builder.setNegativeButton("No",
                    (DialogInterface dialog, int id1) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToLeaderboard(View view) {
        Intent intent = new Intent(this, Leaderboard.class);
        startActivity(intent);
    }

    public void goToPrediction(View view) {
        if (isMatchToday) {
            Intent intent = new Intent(this, Predictions.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No match today", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            String shareBody = "https://nome.ga/myipl";

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_rules) {
            Intent intent = new Intent(this, Rules.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_fixtures) {
            Intent intent = new Intent(this, Fixtures.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.contact_us) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"myiplapp@gmail.com"});
            startActivity(Intent.createChooser(intent, "Send email..."));
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, AboutUs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refreshTime() {
        thread = new Thread(() -> {
            try {
                while (shouldThreadRun) {
                    runOnUiThread(() ->
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.of(13, 0)) &&
                                    LocalTime.now(ZoneId.of("Asia/Kolkata")).isBefore(LocalTime.of(14, 0))) {
                                timeLeft.setText(MINUTES.between(LocalTime.now(ZoneId.of("Asia/Kolkata")), LocalTime.of(14, 1)) + " minutes left untill you can give your prediction(s).");
                                timeLeft.setVisibility(View.VISIBLE);
                            } else if (LocalTime.now(ZoneId.of("Asia/Kolkata")).isAfter(LocalTime.of(14, 0))) {
                                timeLeft.setText("Prediction is now disabled");
                                timeLeft.setVisibility(View.VISIBLE);
                            } else {
                                timeLeft.setVisibility(View.GONE);
                            }
                        } else {
                            timeLeft.setVisibility(View.GONE);
                        }
                    });
                    Thread.sleep(30000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
