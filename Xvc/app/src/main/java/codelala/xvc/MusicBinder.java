package codelala.xvc;

import android.content.Context;
import android.os.Binder;
import android.os.Message;

import music.MusicController;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class MusicBinder extends Binder {
    private MusicController mMusicController = MusicController.getInstance();

    public MusicBinder(Context context) {
        mMusicController.init(context);
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMusicController.handleMsg(msg);
        }
    };

    public void sendMsg(Object obj, int... what){
        Message msg = mHandler.obtainMessage();
        if (what != null) {
            int len = what.length;
            msg.what = (len > 0 ? what[0] : MusicCommand.SYSTEM_ERROR);
            msg.arg1 = (len > 1 ? what[1] : MusicCommand.SYSTEM_ERROR);
            msg.arg2 = (len > 2 ? what[2] : MusicCommand.SYSTEM_ERROR);
        } else {
            msg.what = MusicCommand.SYSTEM_ERROR;
        }
        if (obj == null) {
            mHandler.sendEmptyMessage(msg.what);
        } else {
            msg.obj = obj;
            mHandler.sendMessage(msg);
        }
    }
    public void sendMsg(int what){
        sendMsg(null, what);
    }
    public void sendMsg(int what, int arg1) {
        sendMsg(null, what, arg1);
    }
}
