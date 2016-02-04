package codelala.xvc;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Administrator on 2016/1/10 0010.
 */
public final class MusicUtils {

    private static boolean DEBUG = true;

    private static int mScreenY = -1;

    public static <T extends View> T get(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static void log(String str) {
        if (DEBUG) Log.v("xieyanx", str);
    }

    public static void toast(Context context, String str) {
        if (DEBUG) Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static String formatMusicTime(int duration) {
        duration = duration/1000; //seconds
        String min, ss;
        int sec = (int) Math.ceil(duration%60);
        if (sec == 0) {
            ss = "00";
        } else {
            ss = String.valueOf(sec);
            if (ss.length() == 1) {
                ss = "0" + ss;
            }
        }
        if (duration > 60) {
            min = String.valueOf((int) Math.floor(duration/60)) + ":";
        } else {
            min = "00:";
        }
        return (min + ss);
    }

    public static int getRandomIndex(int len) {
        Random r = new Random();
        return r.nextInt(len);
    }

    public static int getLastIndex(int cur, int len) {
        cur--;
        if (cur < 0) {
            cur = len - 1;
        }
        return cur;
    }

    public static int getNextIndex(int cur, int len) {
        cur++;
        if (cur >= len) {
            cur = 0;
        }
        return cur;
    }

    public static int setYLocation(View view, int y) {
        if (view.getY() == 0) {
            y -= view.getHeight();
            setTranslationY(view, y);
        }
        return y;
    }

    public static void setTranslationY(View view ,int y) {
        view.setTranslationY(y);
    }

    public static int getScreenHeight(Context context) {
        if (mScreenY < 0) {
            Point size = new Point();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mScreenY = (size.y - context.getResources().getDimensionPixelSize(resourceId));
            } else {
                mScreenY = (size.y - 50);
            }
        }
        return mScreenY;
    }

}
