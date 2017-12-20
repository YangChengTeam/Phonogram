package com.yc.phonogram.bean;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 */

public class LPContntInfo {

    private String lpName;
    private String lpContent;
    private  int lpStart;
    private  int lpEnd;

    public int getLpStart() {
        return lpStart;
    }

    public void setLpStart(int lpStart) {
        this.lpStart = lpStart;
    }

    public int getLpEnd() {
        return lpEnd;
    }

    public void setLpEnd(int lpEnd) {
        this.lpEnd = lpEnd;
    }

    public String getLpName() {
        return lpName;
    }

    public void setLpName(String lpName) {
        this.lpName = lpName;
    }

    public String getLpContent() {
        return lpContent;
    }

    public void setLpContent(String lpContent) {
        this.lpContent = lpContent;
    }
}
