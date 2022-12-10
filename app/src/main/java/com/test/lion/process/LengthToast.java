package com.test.lion.process;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class LengthToast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String str = "";
        String sharedText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            str = sharedText + "";
        } else {
            try {
                str = getIntent()
                        .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT) + "";
            } catch (Exception e) {
                Log.i("thisthis", e + "");
                finish();
            }
        }
        final EditText edit1 = new ClearEditText(this);
        edit1.setText("");
        edit1.setHint("Find Content");
        edit1.setTextColor(Color.BLACK);
        final String strl = str;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);//, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edit1.getText().toString().length() == 0)
                    Toast.makeText(LengthToast.this, "길이 : " + strl.length(), Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(LengthToast.this, "길이 : " + (strl.split(edit1.getText() + "", -1).length - 1), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setView(edit1, 50, 40, 50, 0);
        dialog.setTitle("Length");
        dialog.setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
