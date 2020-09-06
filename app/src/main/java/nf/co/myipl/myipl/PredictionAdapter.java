package nf.co.myipl.myipl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

class PredictionAdapter extends ArrayAdapter<String> {

    private final Context ctx;
    private final ArrayList<String> username;
    private final ArrayList<String> umatch1;
    private final ArrayList<String> umatch2;
    private final String status;

    PredictionAdapter(Context ctx, ArrayList<String> username, ArrayList<String> umatch1, ArrayList<String> umatch2, String status) {
        super(ctx, R.layout.adapter_prediction, username);
        this.ctx = ctx;
        this.username = username;
        this.umatch1 = umatch1;
        this.umatch2 = umatch2;
        this.status = status;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rootView = convertView;

        if (rootView == null)
            rootView = LayoutInflater.from(ctx).inflate(R.layout.adapter_prediction, parent, false);

        TextView name = rootView.findViewById(R.id.rank);
        TextView match1 = rootView.findViewById(R.id.username);
        TextView match2 = rootView.findViewById(R.id.points);
        UserInfo userInfo = new UserInfo(ctx);

        int count2a = 0, count2b = 0, count1a = 0, count1b = 0;
        if(status.equals("dead"))
        {
            for (int i = 0; i < umatch1.size(); i++) {
                umatch1.set(i,"--");
                umatch2.set(i,"--");
            }
        }

        for (int i = 0; i < umatch1.size(); i++) {
            if (umatch1.get(i).equals(userInfo.getMATCH2a()))
                count2a++;
            else if (umatch1.get(i).equals(userInfo.getMATCH2b()))
                count2b++;
            if (umatch2.get(i).equals(userInfo.getMATCH1a()))
                count1a++;
            else if (umatch2.get(i).equals(userInfo.getMATCH1b()))
                count1b++;
        }

        name.setText(username.get(position));
        match1.setText(umatch1.get(position));
        Predictions.match2a.setText(userInfo.getMATCH2a() + " : " + count2a);
        Predictions.match2b.setText(userInfo.getMATCH2b() + " : " + count2b);
        Predictions.ngiven2.setText("Not given : " + (umatch1.size() - count2a - count2b));
        if (new UserInfo(ctx).getMATCH_COUNT().equals("2")) {
            Predictions.match1a.setText(userInfo.getMATCH1a() + " : " + count1a);
            Predictions.match1b.setText(userInfo.getMATCH1b() + " : " + count1b);
            Predictions.ngiven1.setText("Not given : " + (umatch2.size() - count1a - count1b));
            match2.setVisibility(View.VISIBLE);
            match2.setText(umatch2.get(position));
        } else
            match2.setVisibility(View.GONE);

        return rootView;
    }
}