package com.yc.phonogram.ui.activitys;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.kk.utils.ScreenUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.ui.popupwindow.SharePopupWindow;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//

    }

    public void share(View view) {
        SharePopupWindow sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.showAtLocation(findViewById(R.id.container), Gravity.CENTER, 0, 0);
//        sharePopupWindow.setWidth(ScreenUtil.getWidth(this));
    }

}
