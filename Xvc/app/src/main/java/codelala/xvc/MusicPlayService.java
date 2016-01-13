package codelala.xvc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Handler;
import music.MusicController;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class MusicPlayService extends Service {

    private MusicBinder mMusicBinder;
    private MusicController mMusicController = MusicController.getInstance();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMusicController.handleMsg(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicController.init(this);
    }

    public class MusicBinder extends Binder {
        public void sendMsg(Object obj, int... what){
            Message msg = mHandler.obtainMessage();
            if (what != null) {
                int len = what.length;
                msg.what = (len > 0 ? what[0] : Command.SYSTEM_ERROR);
                msg.arg1 = (len > 1 ? what[1] : Command.SYSTEM_ERROR);
                msg.arg2 = (len > 2 ? what[2] : Command.SYSTEM_ERROR);
            } else {
                msg.what = Command.SYSTEM_ERROR;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mMusicBinder == null) mMusicBinder = new MusicBinder();
        return mMusicBinder;
    }

}
