package music;

import android.content.Context;
import android.media.MediaPlayer;
import java.io.IOException;
import java.util.Random;
import codelala.xvc.Command;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class MusicController {

    private MediaPlayer mMediaPlayer  = new MediaPlayer();;
    private static MusicController instance = new MusicController();
    private MusicInfo mMusicInfo = MusicInfo.getInstance();
    private boolean mPause = true;

    private MusicController(){}

    public static MusicController getInstance() {
        return instance;
    }

    public void init() {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                mPause = false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setDataSource(mMusicInfo.getDataSource(Command.AUTO_PLAY));
            }
        });
    }

    public final void clickAction(int arg) {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPause = true;
        } else {
            if (mPause) {
                mMediaPlayer.start();
                mPause = false;
            } else {
                setDataSource(mMusicInfo.getDataSource(arg));
            }
        }
    }

    private final void setDataSource(String dataSource) {
        mMediaPlayer.reset();
        mPause = true;
        try {
            mMediaPlayer.setDataSource(dataSource);
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
