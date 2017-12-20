package com.xinqu.videoplayer.manager;

import com.xinqu.videoplayer.XinQuVideoPlayer;

/**
 * Put JZVideoPlayer into layout
 * From a JZVideoPlayer to another JZVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class XinQuVideoPlayerManager {

    public static XinQuVideoPlayer FIRST_FLOOR_JZVD;
    public static XinQuVideoPlayer SECOND_FLOOR_JZVD;

    public static XinQuVideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JZVD;
    }

    public static void setFirstFloor(XinQuVideoPlayer xinQuVideoPlayer) {
        FIRST_FLOOR_JZVD = xinQuVideoPlayer;
    }

    public static XinQuVideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JZVD;
    }

    public static void setSecondFloor(XinQuVideoPlayer xinQuVideoPlayer) {
        SECOND_FLOOR_JZVD = xinQuVideoPlayer;
    }

    public static XinQuVideoPlayer getCurrentJzvd() {
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
