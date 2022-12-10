package com.test.lion.process;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProcessTextActivity extends AppCompatActivity {

    CharSequence text;
    boolean bol = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.content_process_text);
        //boolean readonly = getIntent ()
        //        .getBooleanExtra (Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        if (text == null) {
            text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            bol = true;
        }
        //colorControlActivated
        //textColorHighlight
        //textCursorDrawable

        final TextInputLayout editL = new TextInputLayout(this);
        final EditText edit = new ClearEditText(this);
        edit.setText("");
        edit.setHint("Replace RegExp");
        edit.setTextColor(Color.BLACK);
        editL.addView(edit);
        final TextInputLayout editL2 = new TextInputLayout(this);
        final EditText edit1 = new ClearEditText(this);
        edit1.setText("");
        edit1.setHint("Replace Content");
        edit1.setTextColor(Color.BLACK);
        editL2.addView(edit1);
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(false);
        checkBox.setText("Trim All Line");
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(1);
        lay.addView(editL);
        lay.addView(editL2);
        lay.addView(checkBox);


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);//, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                try {
                    String res = text.toString().replaceAll(edit.getText().toString(), edit1.getText().toString());
                    if (bol) {
                        if (checkBox.isChecked())
                            copes(trimAllLine(res));
                        else
                            copes(res);
                    } else {
                        if (checkBox.isChecked())
                            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, trimAllLine(res));
                        else
                            intent.putExtra(Intent.EXTRA_PROCESS_TEXT, res);
                    }
                } catch (Exception e) {
                    Log.i("thisthis", e + "");
                    finish();
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setView(lay, 50, 40, 50, 0);
        dialog.setTitle("Replace");
        dialog.setCancelable(false);
        AlertDialog alert = dialog.create();
        alert.show();
    }

    public void copes(String sr) {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", sr);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ProcessTextActivity.this, "copied", Toast.LENGTH_SHORT).show();
    }

    public String trimAllLine(String str) {
        String[] arr2 = str.split("\n");
        String arr = arr2[0].trim();
        for (int a = 1; a < arr2.length; a++)
            arr += "\n" + arr2[a].trim();
        return arr;
    }
}
