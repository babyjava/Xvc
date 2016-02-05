package codelala.xvc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public final class MusicPlayService extends Service {

    private MusicBinder mMusicBinder;
    private MusicController mMusicController;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicController = MusicController.getInstance();
        mMusicController.init(this);
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMusicController.handleMsg(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mMusicBinder == null) {
            mMusicBinder = new MusicBinder();
        }
        return mMusicBinder;
    }

    public class MusicBinder extends Binder {

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

}
