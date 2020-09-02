package nf.co.myipl.myipl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class Utility {
    public static String url_host = "http://192.168.0.109:8080";

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data.append(line);
            }
            Log.v("Utility(getData()) : ", "Data : " + data.toString());
            httpURLConnection.disconnect();
            inputStream.close();
            bufferedReader.close();
            return data.toString();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("Utility(getData()) : URL : " + url.toString() + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.v("Utility(getData()) : ", "Error Message : " + e.getMessage());
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
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
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
            Log.v("Utility(postData()) : ", "Data : " + data.toString());
            bufferedReader.close();
            httpURLConnection.disconnect();
            return data.toString();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            FirebaseCrashlytics.getInstance().log("Utility(postData()) : URL : " + url.toString() + " Exception : " + e.getMessage());
            e.printStackTrace();
            Log.v("Utility(postData()) : ", "Error Message : " + e.getMessage());
        }
        return null;
    }
}
