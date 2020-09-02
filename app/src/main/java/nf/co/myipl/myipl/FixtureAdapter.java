package nf.co.myipl.myipl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FixtureAdapter extends ArrayAdapter<String> {

    private final Context ctx;
    private final ArrayList<String> date;
    private final ArrayList<String> match1;
    private final ArrayList<String> match2;
    private final ArrayList<String> winner;

    FixtureAdapter(Context ctx, ArrayList<String> date, ArrayList<String> match1, ArrayList<String> match2, ArrayList<String> winner) {
        super(ctx, R.layout.adapter_fixture, match1);
        this.ctx = ctx;
        this.date = date;
        this.match1 = match1;
        this.match2 = match2;
        this.winner = winner;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(ctx).inflate(R.layout.adapter_fixture, parent, false);

        TextView dateText = rootView.findViewById(R.id.dateText);
        TextView match1Text = rootView.findViewById(R.id.match1Text);
        TextView match2Text = rootView.findViewById(R.id.match2Text);
        TextView winnerText = rootView.findViewById(R.id.winnerText);
        dateText.setText(date.get(position));
        match1Text.setText(match1.get(position));
        match2Text.setText(match2.get(position));
        winnerText.setText(winner.get(position));

        return rootView;
    }

}
