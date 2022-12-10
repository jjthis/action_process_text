package com.test.lion.process;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class dictionary extends AppCompatActivity {

    static String str;
    NotificationCompat.Action replyAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            text = (CharSequence) sharedText;
            str = translate(sharedText);
        } else {
            try {
                str = translate(text.toString()).split("<td background=.*?>")[1].split("<table")[0].replaceAll("<.*?>", "").replaceAll(text + "[^’]*?&nbsp;&nbsp;&nbsp;", "\n" + text + ": ").replace("&nbsp;&nbsp;&nbsp;", "");
            } catch (Exception e) {
                Log.i("thisthis", e + "");
                finish();
            }
        }
        Intent intent;
        if (RemoteInput.getResultsFromIntent(intent = getIntent()) != null) {
            copytext(intent);
            return;
        }
        String replyLabel = "Enter your order here";

        //Initialise RemoteInput
        RemoteInput remoteInputs = new RemoteInput.Builder("key reply")
                .setLabel(replyLabel)
                .build();

        //PendingIntent that restarts the current activity instance.
        Intent resultIntent = new Intent(this, dictionary.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("Data", str);
        resultIntent.putExtra("Data2", text);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 6, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Notification Action with RemoteInput instance added.
        replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat, "복사", resultPendingIntent)
                .addRemoteInput(remoteInputs)
                .setAllowGeneratedReplies(true)
                .build();

        //Notification.Action instance added to Notification Builder.

        NotificationCompat.Builder notice = new NotificationCompat.Builder(this);
        notice.setPriority(2);
        notice.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        notice.setSmallIcon(R.mipmap.ic_launcher);
        notice.setVisibility(1);
        notice.setCategory("msg");
        notice.setContentTitle("사전");
        notice.setContentText(str);
        notice.addAction(replyAction);
        notice.addAction(1, "더보기", PendingIntent.getActivity
                (this, 0, new Intent(this, dialogView.class).putExtra("Data", str), PendingIntent.FLAG_UPDATE_CURRENT));
        notice.setStyle(new NotificationCompat.BigTextStyle().bigText(str));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(5, notice.build());

        finish();
    }

    public static String translate(String strt) {
        URL url = null;
        try {
            url = new URL("http://stdweb2.korean.go.kr/search/List_dic.jsp?PageRow=10&Table=words%7Cword&Gubun=0&SearchPart=Simple&SearchText=" + strt);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "skjasfhjha";
    }

    public void copytext(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {
            String str = intent.getStringExtra("Data");
            String str2 = intent.getStringExtra("Data2");
            CharSequence reply = remoteInput.getCharSequence("key reply");
            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", str2 + str.split(str2)[Integer.parseInt((String) reply) + 1]);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "copied", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(5);
            finish();
            return;
        }
    }
}
