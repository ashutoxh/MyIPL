package nf.co.myipl.myipl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<String> {

    private final Context ctx;
    private final ArrayList<String> uname;
    private final ArrayList<String> upoints;

    LeaderboardAdapter(Context ctx, ArrayList<String> uname, ArrayList<String> upoints) {
        super(ctx, R.layout.adapter_prediction, uname);
        this.ctx = ctx;
        this.uname = uname;
        this.upoints = upoints;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(ctx).inflate(R.layout.adapter_leaderboard, parent, false);

        TextView rankT = rootView.findViewById(R.id.rank);
        TextView username = rootView.findViewById(R.id.username);
        TextView points = rootView.findViewById(R.id.points);
//        double point;
//        int count = 0;

        username.setText(uname.get(position));
        points.setText(upoints.get(position));
        ArrayList<Integer> rank = new ArrayList<>();
        int p_rank = 1, j;
        rank.add(p_rank);
        for (int i = 0; i < upoints.size() - 1; i++) {
            j = i + 1;
            if (Double.parseDouble(upoints.get(i)) == Double.parseDouble(upoints.get(j))) {
                rank.add(p_rank);
                Log.d("Rank  is ", "" + p_rank);
            } else {
                p_rank += 1;
                rank.add(p_rank);
                Log.d("Rank else is ", "" + p_rank);
            }
        }
        rankT.setText("" + rank.get(position));
        switch (rank.get(position)) {
            case 1:
                rankT.setTypeface(null, Typeface.BOLD);
                username.setTypeface(null, Typeface.BOLD);
                points.setTypeface(null, Typeface.BOLD);

                rankT.setTextSize(15);
                username.setTextSize(15);
                points.setTextSize(15);

                rankT.setTextColor(Color.parseColor("#DAA520"));
                username.setTextColor(Color.parseColor("#DAA520"));
                points.setTextColor(Color.parseColor("#DAA520"));
                break;
            case 2:
                rankT.setTypeface(null, Typeface.BOLD);
                username.setTypeface(null, Typeface.BOLD);
                points.setTypeface(null, Typeface.BOLD);

                rankT.setTextSize(15);
                username.setTextSize(15);
                points.setTextSize(15);

                rankT.setTextColor(Color.parseColor("#A9A9A9"));
                username.setTextColor(Color.parseColor("#A9A9A9"));
                points.setTextColor(Color.parseColor("#A9A9A9"));
                break;
            case 3:
                rankT.setTypeface(null, Typeface.BOLD);
                username.setTypeface(null, Typeface.BOLD);
                points.setTypeface(null, Typeface.BOLD);

                rankT.setTextSize(15);
                username.setTextSize(15);
                points.setTextSize(15);

                rankT.setTextColor(Color.parseColor("#8C7853"));
                username.setTextColor(Color.parseColor("#8C7853"));
                points.setTextColor(Color.parseColor("#8C7853"));
                break;
            default:
                rankT.setTypeface(null, Typeface.NORMAL);
                username.setTypeface(null, Typeface.NORMAL);
                points.setTypeface(null, Typeface.NORMAL);

                rankT.setTextColor(Color.parseColor("#000000"));
                username.setTextColor(Color.parseColor("#000000"));
                points.setTextColor(Color.parseColor("#000000"));
                break;
        }

        return rootView;
    }

}
