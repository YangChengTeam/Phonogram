package com.yc.phonogram.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import com.kk.utils.TaskUtil;

/**
 * Created by zhangkai on 2017/12/18.
 */

public class Mp3Utils {
    private static MediaPlayer player;

    private static void prepareMp3(final Context context) {
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                try {
                    AssetManager assetManager = context.getAssets();
                    AssetFileDescriptor afd = assetManager.openFd("sound.mp3");
                    if (player == null) {
                        player = new MediaPlayer();
                    }
                    player.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
                    player.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void playMp3() {
        if (player != null) {
            player.start();
        }
    }

    public static void stop() {
        if (player != null) {
            player.stop();
        }
    }

}
