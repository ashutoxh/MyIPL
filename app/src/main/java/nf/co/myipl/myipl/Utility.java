package nf.co.myipl.myipl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class Utility {
    public static String url_host = "http://192.168.0.109:8080/myipl";      //ASHU
//    public static String url_host = "http://192.168.0.102:8080/myipl";            //AWES

    public static boolean connection(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getData(String url) {
        StringBuilder data = new StringBuilder();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(10000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
            Log.d("Utility(getData()) : ", "Data : " + data.toString());
            httpURLConnection.disconnect();
            inputStream.close();
            bufferedReader.close();
            return data.toString();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("Utility(getData()) : URL : " + url + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.d("Utility(getData()) : ", "Error Message : " + e.getMessage());
        }
        return null;
    }

    public static String postData(String url, String dataToPost) {
        StringBuilder data = new StringBuilder();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(10000);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write(dataToPost);
            bufferedWriter.flush();
            bufferedWriter.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
            Log.d("Utility(postData()) : ", "Data : " + data.toString());
            bufferedReader.close();
            httpURLConnection.disconnect();
            return data.toString();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("Utility(postData()) : URL : " + url + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.d("Utility(postData()) : ", "Error Message : " + e.getMessage());
        }
        return null;
    }

    public static String getCurrentDateTime() {
        String time_url = Utility.url_host + "/utility/getCurrentDateTime";
        return getData(time_url);
    }

    public static String getTime() {
        String response = getCurrentDateTime();
        String time;
        if (response != null) {
            JSONObject JO;
            try {
                JO = new JSONObject(response);
                if ("success".equals(JO.getString("action"))) {
                    time = JO.getString("message");
                    time = time.substring(time.length() - 5, time.length() - 3);
                    Log.d("Utility(getTime())", "time : " + time);
                    return time;
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("Utility(getTime()) : Exception : " + e.getMessage());
                e.printStackTrace();
                Log.d("Utility(getTime())", "Error Message : " + e.getMessage());
            }
        }
        return null;
    }

    public static String getDate() {
        String response = getCurrentDateTime();
        Log.d("Utility(getDate())", "response : " + response);
        String date;
        if (response != null) {
            try {
                JSONObject JO = new JSONObject(response);
                Log.d("Utility(getDate())", "JO : " + JO.toString());
                if ("success".equals(JO.getString("action"))) {
                    date = JO.getString("message");
                    date = date.substring(0, 10);
                    Log.d("Utility(getDate())", "date : " + date);
                    return date;
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                FirebaseCrashlytics.getInstance().log("Utility(getDate()) : Exception : " + e.getMessage());
                e.printStackTrace();
                Log.d("Utility(getDate())", "Error Message : " + e.getMessage());
            }
        }
        return null;
    }
}
