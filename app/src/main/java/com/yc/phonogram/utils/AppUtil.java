package com.yc.phonogram.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.kk.utils.ToastUtil;

/**
 * Created by zhangkai on 2017/10/25.
 */

public class AppUtil {
    public static void gotoWeiXin(Context context) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui" +
                    ".LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void gotoWeiXin(final Context context, String msg) {
        gotoWeiXin(context, msg, null);
    }

    public static void gotoWeiXin(final Context context, String msg, final Runnable runnable) {
        ToastUtil.toast2(context, msg);
        UIUtil.postDelayed(2000, new Runnable() {
            @Override
            public void run() {
                AppUtil.gotoWeiXin(context);
                if (runnable != null) runnable.run();
            }
        });
    }

    public static void gotoQQ(final Context context, String qq) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Toast.makeText(context, "手机QQ未安装或该版本不支持", Toast.LENGTH_SHORT).show();
        }
    }

    public static void copy(Context context, String data) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", data);
        if(clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    public static void openWxShareText(final Activity ctx, String content) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
            ctx.startActivity(intent);
        } catch (Exception e) {
            AppUtil.gotoWeiXin(ctx, "下载地址已复制, 正在前往微信...");
        }
    }

    public static boolean checkQQInstalled(Context context) {
        Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    private static final int REQUEST_CODE = 1;

    public static boolean checkPermission(Activity context, String permission, String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED)) {
                if (context.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
                requestMultiplePermissions(context, permission);
                return false;
            }
            return true;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestMultiplePermissions(Activity context, String permission) {
        String[] permissions = {permission};
        context.requestPermissions(permissions, REQUEST_CODE);
    }
}
