package codelala.xvc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/10 0010.
 */
public final class Utils {

    private final static boolean DEBUG = true;

    public final static <T extends View> T get(View view, int id) {
        return (T) view.findViewById(id);
    }

    public final static void log(String str) {
        if (DEBUG) Log.v("xieyan", str);
    }

    public final static void toast(Context context, String str) {
        if (DEBUG) Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

}
