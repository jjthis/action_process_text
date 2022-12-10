package com.test.lion.process;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Tile extends TileService {

    @Override
    public void onClick() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("replace.text.com.replaceregexp");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityAndCollapse(intent);

    }
}
