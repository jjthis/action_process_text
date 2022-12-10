package com.test.lion.process;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class dialogView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView showText = new TextView(this);
        Intent intent = getIntent();
        showText.setText(intent.getStringExtra("Data").trim());
        showText.setTextColor(Color.BLACK);
        showText.setTextSize(15);
        showText.setTextIsSelectable(true);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setTitle("사전");
        dialog.setCancelable(false);
        //dialog.setView(container);
        dialog.setView(showText, 80,40,80,0);
        AlertDialog alert = dialog.create();
        alert.show();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);
    }
}
