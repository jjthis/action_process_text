package com.test.lion.process;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView text;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(new NotificationChannel(
                            "com.test.lion.process", "jabdongsani",
                            NotificationManager.IMPORTANCE_DEFAULT
                    ));
        }
        edit = new EditText(this);
        edit.setText("인생");
        text = new TextView(this);
        text.setText("인생");
        text.setTextSize(55);
        text.setTextIsSelectable(true);
        edit.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });
        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(edit);
        lay.addView(text);
        setContentView(lay);
    }

    @Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        //mode.getMenu().clear();
        Menu menus = mode.getMenu();
        onInitializeMenu(menus);
        super.onActionModeStarted(mode);
    }

    private List<ResolveInfo> getSupportedActivities() {
        PackageManager packageManager =
                text.getContext().getPackageManager();
        return
                packageManager.queryIntentActivities(createProcessTextIntent(),
                        0);
    }

    private Intent createProcessTextIntent() {
        return new Intent()
                .setAction(Intent.ACTION_PROCESS_TEXT)
                .setType("text/plain");
    }

    private Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
        return createProcessTextIntent()
                .putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false)
                .setClassName(info.activityInfo.packageName,
                        info.activityInfo.name);
    }

    public void onInitializeMenu(Menu menu) {
        int menuItemOrder = 100;
        for (ResolveInfo resolveInfo : getSupportedActivities()) {
            menu.add(Menu.NONE, Menu.NONE,
                    menuItemOrder++,
                    resolveInfo.loadLabel(this.getPackageManager()))//.setIcon(resolveInfo.loadIcon(getPackageManager()))
                    .setIntent(createProcessTextIntentForResolveInfo(resolveInfo))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }




}
