package com.yc.phonogram.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import yc.com.rthttplibrary.util.TaskUtil;


/**
 * Created by zhangkai on 2017/12/18.
 */

public class Mp3Utils {

    public static void playMp3(Context context, String name) {
        playMp3(context, name, null);
    }

    public static MediaPlayer playMp3(final Context context,final String name,  final MediaPlayer.OnCompletionListener
            completionListener) {
        final MediaPlayer player = new MediaPlayer();
        TaskUtil.getImpl().runTask(() -> {
            try {
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor afd = assetManager.openFd(name);

                player.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(), afd.getLength());
                player.prepareAsync();
                player.setOnPreparedListener(mp -> mp.start());
                if(completionListener != null) {
                    player.setOnCompletionListener(completionListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return player;
    }


}
