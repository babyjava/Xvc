package codelala.xvc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public final class MusicPlayService extends Service {

    private final MusicBinder mMusicBinder = new MusicBinder(this);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

}
