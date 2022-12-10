package com.test.lion.process;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class morseCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if(sharedText == null) {
            Intent intent = new Intent();
            try {
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, tests(text.toString()).trim());
            } catch (Exception e) {
                Log.i("thisthis", e + "");
                finish();
            }
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            String str = tests(sharedText).trim();
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
            notice.setContentTitle("모스부호");
            notice.setContentText(str);
            notice.addAction(1, "공유", share);
            notice.addAction(1, "복사", PendingIntent.getActivity
                    (this, 0, new Intent(this, copy.class).putExtra("Data", str), PendingIntent.FLAG_UPDATE_CURRENT));
            notice.setStyle(new Notification.BigTextStyle().bigText(str));
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2, notice.build());

            finish();
        }
    }

    String[][] mos = MyAssets.mos;

    int BASE_CODE = 44032;
    int CHOSUNG = 588;
    int JUNGSUNG = 28;

    // 초성 리스트. 00 ~ 18
    char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    // 중성 리스트. 00 ~ 20
    char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    // 종성 리스트. 00 ~ 27 + 1(1개 없음)

    char[] JONGSUNG_LIST = {' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    public String cjjconv(String strs) {
        String foo = "";
        // BASE_CODE(4403244) 제거
        char c_Temp = strs.charAt(0);
        if (c_Temp > 44031 && c_Temp < 55204) {
            int cBase = c_Temp - BASE_CODE;
            // 초성

            int c1 = (int) Math.floor(cBase / CHOSUNG);
            foo += CHOSUNG_LIST[c1];
            // 종성

            int c2 = (int) Math.floor((cBase - (CHOSUNG * c1)) / JUNGSUNG);
            foo += JUNGSUNG_LIST[c2];
            // 종성

            int c3 = (int) Math.floor((cBase - (CHOSUNG * c1) - (JUNGSUNG * c2)));
            foo += JONGSUNG_LIST[c3];
            return foo;
        } else {
            return strs;
        }
    }


    public String tests(String ss) {
        char[] sss = ss.toCharArray();
        Log.i("thisthiss", "tests: "+sss[0]);
        String res = "";
        for (int a = 0; a < sss.length; a++) {
            String ssss = Character.toString(sss[a]);
            Log.i("thisthiss", "tests: "+ssss);
            String[] cs = cjjconv(ssss)
                    .split("");
            for (int c = 0; c < cs.length; c++) {
                if (ssss == " ") {
                    res += "      ";
                    continue;
                }
                for (int b = 0; b < mos.length; b++) {
                    if (cs[c].equalsIgnoreCase(mos[b][0])) {
                        Log.i("thisthiss", "tests: "+mos[b][1]);
                        res += mos[b][1] + "   ";
                        b = mos.length;
                    }
                }
            }
        }
        return res;
    }
}
