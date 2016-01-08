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

    private final MusicBinder mMusicBinder = new MusicBinder();
    private MusicController mMusicController = MusicController.getInstance();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMusicController.clickAction(msg.arg1);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicController.init();
    }

    private class MusicBinder extends Binder {
        public void justClick(int arg1){
            mHandler.sendEmptyMessage(arg1);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

}
