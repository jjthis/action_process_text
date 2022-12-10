package com.test.lion.process;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Translation extends AppCompatActivity {
    static String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);





        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            str = translate(sharedText);
        } else{
            try {
                CharSequence text = getIntent()
                        .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
                str = translate(text.toString());
            }catch (Exception e){
                Log.i("thisthis", e+"");
                finish();
            }
        }


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        PendingIntent share = PendingIntent.getActivity(this, 0, Intent.createChooser(sendIntent, str), PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder notice = new Notification.Builder(this,"com.test.lion.process");
        notice.setPriority(2);
        notice.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        notice.setSmallIcon(R.mipmap.ic_launcher);
        notice.setVisibility(1);
        notice.setCategory("msg");
        notice.setContentTitle("번역");
        notice.setContentText(str);
        notice.addAction(1, "공유", share);
        notice.addAction(1, "복사", PendingIntent.getActivity
                (this, 0, new Intent(this, copy.class).putExtra("Data", str), PendingIntent.FLAG_UPDATE_CURRENT));
        notice.setStyle(new Notification.BigTextStyle().bigText(str));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, notice.build());

        finish();
    }

    public static String translate(String strt) {
        URL url = null;
        try {
            url = new URL("http://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=ko&dt=t&q="
                    + URLEncoder.encode(strt, "UTF-8") + "&ie=UTF-8&oe=UTF-8");
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString().split("\\[\\[\"")[1].split("\",\"")[0];
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "skjasfhjha";
    }


}
