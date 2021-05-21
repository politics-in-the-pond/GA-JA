package gachon.termproject.gaja.login;

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpConn {
    public void sendData(String to, String title, String message, String postno) {
        new Thread() {
            public void run() {
                try {
                    requestWebServer(to, title, message, postno);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void requestWebServer(String to, String title, String message, String postno) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"to\": \"eDyiJer6SrmYDHFNrnmDR_:APA91bGFij7TKKZpMEiPI6uV_e79PmcMLp0JI5vYzfgI5BpQQcXqiU-Vt58ZY7bY_ooK3H6XrzcTCYzS5CrcGgrhNHDwCLuY2be_nFxd12kW0m3MyN5aTwDyACxKW6ZGlGDoDf7ixlxe\",\r\n    \"priority\": \"high\",\r\n    \"data\": {\r\n        \"title\": \"" + title + "\",\r\n        \"message\": \"" + message +"\",\r\n        \"postno\": \"" + postno + "\"\r\n    }\r\n}");
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .method("POST", body)
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "key=AAAAmRVHygs:APA91bEWYinvn4KaWHz3u_fmQKsZmJkGHH9jAuB1klHQFVybEOfFmvDQ4x7M77REPBhhpPfuJens2-xCb7EYeMr_GOy3IG-G03ujjqruP3rc7IaRx1VAMng37Wegy92U1RB2JfYm0mg1")
                .build();
        Response response = client.newCall(request).execute();
    }
}
