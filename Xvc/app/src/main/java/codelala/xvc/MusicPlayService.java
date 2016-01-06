package codelala.xvc;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import java.io.IOException;
import android.os.Handler;

import data.MusicInfo;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class MusicPlayService extends Service {

    private MediaPlayer mMediaPlayer;
    private String[][] mMusicInfo;
    private final MusicBinder mMusicBinder = new MusicBinder();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case Command.MUSIC_PLAY:
                    musicPlay();
                    break;
                case Command.MUSIC_PAUSE:
                    musicPlay();
                    break;
                case Command.MUSIC_NEXT:
                    musicPlay();
                    break;
                case Command.MUSIC_LAST:
                    musicPlay();
                    break;
                default:
                    break;
            }
        }
    };

    private MusicInfo.MusicInfoReadyListener mMusicInfoReadyListener = new MusicInfo.MusicInfoReadyListener() {

        @Override
        public void musicInfoReady(String[][] musicInfo) {
            mMusicInfo = musicInfo;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        MusicInfo.getInstance().start(mMusicInfoReadyListener, this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicAutoPlay();
            }
        });
    }

    private final void musicAutoPlay() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class MusicBinder extends Binder {
        public void justDo(int arg1){
            mHandler.sendEmptyMessage(arg1);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBinder;
    }

    private final void musicPlay() {
        if (mMusicInfo == null) return;
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource("");
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
