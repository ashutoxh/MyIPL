package nf.co.myipl.myipl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

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
        if (date.size() != 0 && match1.size() != 0 && match2.size() != 0 && winner.size() != 0) {
            dateText.setText(date.get(position));
            setBackgroundColour(date.get(position), dateText);
            match1Text.setText(match1.get(position));
            setBackgroundColour(match1.get(position), match1Text);
            match2Text.setText(match2.get(position));
            setBackgroundColour(match2.get(position), match2Text);
            winnerText.setText(winner.get(position));
            setBackgroundColour(winner.get(position), winnerText);
        }
        return rootView;
    }

    private void setBackgroundColour(String teamName, TextView teamText){
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(ctx, R.drawable.edittextfix);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        switch (teamName) {
            case "RCB": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.RCB));
                break;
            }
            case "MI": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.MI));
                break;
            }
            case "KKR": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.KKR));
                break;
            }
            case "CSK": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.CSK));
                break;
            }
            case "RR": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.RR));
                break;
            }
            case "SRH": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.SRH));
                break;
            }
            case "DC": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.DC));
                break;
            }
            case "KXIP": {
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.KXIP));
                break;
            }
            default:
                DrawableCompat.setTint(wrappedDrawable, ctx.getResources().getColor(R.color.defaultColor));
        }
        teamText.setBackground(unwrappedDrawable);
    }

}
