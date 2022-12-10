package com.test.lion.process;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

@TargetApi(Build.VERSION_CODES.N)
public class qsTIle extends TileService {
    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        int tileState = tile.getState();
        tile.setState(tileState == Tile.STATE_INACTIVE ? (Tile.STATE_ACTIVE):(Tile.STATE_INACTIVE));
        tile.updateTile();
        if (tileState == Tile.STATE_INACTIVE) {
            // Turn on
            Intent intent = new Intent(getApplicationContext(), wordChainFuck.class);
            startActivity(intent);
            tile.setLabel("실행중");
        } else {
            // Turn off
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(3);
            tile.setLabel("한방 단어");
        }
        tile.updateTile();
    }
}
