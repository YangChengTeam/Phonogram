package com.xinqu.videoplayer.full.manager;
import com.xinqu.videoplayer.full.WindowVideoPlayer;

/**
 * Put JZVideoPlayer into layout
 * From a JZVideoPlayer to another JZVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class WindowVideoPlayerManager {

    public static WindowVideoPlayer FIRST_FLOOR_JZVD;
    public static WindowVideoPlayer SECOND_FLOOR_JZVD;

    public static WindowVideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(WindowVideoPlayer xinQuVideoPlayer) {
        FIRST_FLOOR_JZVD = xinQuVideoPlayer;
    }

    public static WindowVideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(WindowVideoPlayer xinQuVideoPlayer) {
        SECOND_FLOOR_JZVD = xinQuVideoPlayer;
    }

    public static WindowVideoPlayer getCurrentJzvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JZVD != null) {
            SECOND_FLOOR_JZVD.onCompletion();
            SECOND_FLOOR_JZVD = null;
        }
        if (FIRST_FLOOR_JZVD != null) {
            FIRST_FLOOR_JZVD.onCompletion();
            FIRST_FLOOR_JZVD = null;
        }
    }
}
