package com.test.lion.process;

import android.net.Uri;
import android.provider.Settings;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class wordChainFuck extends AppCompatActivity {

    //노티 에딧텍스트 추가 하기!
    NotificationCompat.Action replyAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        processInlineReply(getIntent());
        finish();


    }

    public void processInlineReply(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        String str="한글을 입력!";

        if (remoteInput != null) {
            CharSequence reply = remoteInput.getCharSequence("key reply");
            assert reply != null;
            String str2 = Character.toString(reply.charAt(reply.length() - 1));
            if (str2.matches("[가-힣]")) {
                str = tests(str2);
            } else {
                str = intent.getStringExtra("Data");
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    ClipData clip = ClipData.newPlainText("label", str.split(", ")[Integer.parseInt((String) reply) - 1].replaceAll(".[0-9].", ""));
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "copied", Toast.LENGTH_SHORT).show();
                }catch (Exception ignored){

                }
            }
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        PendingIntent share = PendingIntent.getActivity(this, 0, Intent.createChooser(sendIntent, str), PendingIntent.FLAG_UPDATE_CURRENT);


        String replyLabel = "Enter your order or word here";

        //Initialise RemoteInput
        RemoteInput remoteInputs = new RemoteInput.Builder("key reply")
                .setLabel(replyLabel)
                .build();

        //PendingIntent that restarts the current activity instance.
        Intent resultIntent = new Intent(this, wordChainFuck.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("Data", str);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 5, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Notification Action with RemoteInput instance added.
        replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.sym_action_chat, "order", resultPendingIntent)
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
        notice.setContentTitle("한방단어");
        notice.setContentText(str);
        notice.setOngoing(true);
        Intent notint = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        notint.setData(Uri.parse("package:com.test.lion.process"));
        PendingIntent conin = PendingIntent.getActivity(this.getApplicationContext(), 3, notint, 0);
        notice.setContentIntent(conin);
        notice.addAction(replyAction);
        notice.addAction(1, "공유", share);
        notice.addAction(1, "복사", PendingIntent.getActivity
                (this, 0, new Intent(this, copy.class).putExtra("Data", str), PendingIntent.FLAG_UPDATE_CURRENT));
        notice.setStyle(new NotificationCompat.BigTextStyle().bigText(str));
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotificationManager != null;
        mNotificationManager.notify(3, notice.build());

        finish();
    }


    String[][] word = MyAssets.wordF;

    public String tests(String str) {
        for (int a = 0; a < word.length; a++) {
            if (Character.toString(str.charAt(str.length() - 1)).equals(word[a][0])) {
                return word[a][1];
            }
        }
        return "찾을 수 없음";
    }
}
