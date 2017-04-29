package edu.washington.ischool.pjjj.dynasee;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chelseagjw on 4/29/17.
 */
public final class ToastUtil {
    private static Toast mToast;

    private ToastUtil() {
    }

    public static void show(Context context, String text) {
        if (null == mToast) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }
}
