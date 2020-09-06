package nf.co.myipl.myipl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GivePrediction extends AsyncTask<String, Void, String> {


    static Toast predictionToast;
    static Toast oopsToast;
    static Toast alreadyToast;
    static Toast notYetActiveToast;
    private char[] time;
    @SuppressLint("StaticFieldLeak")
    private Context ctx;

    GivePrediction(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String response_api = "";
        String KEY = "XJFLXL54VT3Z";
        String dateTime;
        String statusTime;
        try {
            URL url = new URL("http://api.timezonedb.com/v2/get-time-zone?key=" + KEY + "&format=json&by=zone&zone=Asia/Kolkata");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuilder data = new StringBuilder();
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
            JSONObject JO = new JSONObject(data.toString());
            dateTime = JO.getString("formatted");
//            dateTime = "2018-04-07 01:29:00";
            time = dateTime.substring(11, 19).toCharArray();
            //Log.d("GivePrediction TIMEEE ","" + time);
            httpURLConnection.disconnect();
            inputStream.close();
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(time != null) {
            Log.d("ARRAYYYYYYYYY : ", "Value : " + Integer.parseInt(("" + time[0]) + ("" + time[1])));
            if (Integer.parseInt(("" + time[0]) + ("" + time[1])) < 2) {
                return "DATA YET TO BE FLUSHED";
            } else if (Integer.parseInt(("" + time[0]) + ("" + time[1])) >= 14) {
                statusTime = "dead";
            } else {
                statusTime = "alive";
            }
            Log.d("GivePrediction : ", statusTime);
            String prediction_url = "https://myipl1-235507.appspot.com/player/prediction";
            if (statusTime.equals("dead")) {
                return "dead";
            } else {
                HttpURLConnection httpURLConnection;
                JSONObject postData = new JSONObject();
                try {
                    Log.d("GivePrediction PARAMS ", "0 " + params[0] + " 1 " + params[1] + " 2 " + params[2]);
                    postData.accumulate("userId", params[0]);
                    if (params[1].equals("match1"))
                        postData.accumulate("match1", params[2]);
                    else
                        postData.accumulate("match2", params[2]);

                    httpURLConnection = (HttpURLConnection) new URL(prediction_url).openConnection();

                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpURLConnection.setRequestProperty("Accept", "application/json; charset=UTF-8");

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));

                    bufferedWriter.write(postData.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder data = new StringBuilder();
                    String line = "";
                    while (line != null) {
                        line = bufferedReader.readLine();
                        data.append(line);
                    }
                    Log.v("GivePrediction : ", "DATA : " + data);
                    JSONObject JO = new JSONObject(data.toString());
                    Log.v("GivePrediction : ", "DATA : " + JO.toString());
                    response_api = JO.getString("action");
                    Log.v("GivePrediction : ", "Response : " + response_api);
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response_api != null) {
                switch (response_api) {
                    case "success":
                        return "alive";
                    case "failure":
                        return "recorded";
                    default:
                        return "Sorry, Our servers are facing some issues.Please try again later";
                }
            }
        }
        return "Sorry, Our servers are facing some issues.Please try again later";
    }


    @Override
    protected void onPostExecute(String status) {
        time = null;
        PostLoginActivity.progressBar.setVisibility(View.GONE);
        switch (status) {
            case "DATA YET TO BE FLUSHED":
                notYetActiveToast = Toast.makeText(ctx, "Predictions will be enabled from 2:00 AM to 2:00 PM", Toast.LENGTH_SHORT);
                notYetActiveToast.show();
                break;
            case "recorded":
                alreadyToast = Toast.makeText(ctx, "Your prediction for this match is already recorded.", Toast.LENGTH_SHORT);
                alreadyToast.show();
                break;
            case "dead":
                oopsToast = Toast.makeText(ctx, "Oops! Looks like you missed the deadline of 2:00 PM", Toast.LENGTH_SHORT);
                oopsToast.show();
                break;
            case "alive":
                predictionToast = Toast.makeText(ctx, "Your prediction for this match is recorded.", Toast.LENGTH_SHORT);
                predictionToast.show();
                break;
            default:
                Toast.makeText(ctx, status, Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
