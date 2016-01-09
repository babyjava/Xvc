package music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;
import codelala.xvc.Command;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class MusicController {

    private MediaPlayer mMediaPlayer  = new MediaPlayer();;
    private static MusicController instance = new MusicController();
    private boolean mPause = true;
    private String[][] mMusicList;
    private Context mContext;
    private MusicInfoReadyListener mMusicInfoReadyListener;

    private String mPlayingMusicInfo;

    private int mPlayMode;
    private int mDataSourceArray;
    private int mPlayMusicIndex;
    private int mMusicListLength;

    private MusicController(){}

    public static MusicController getInstance() {
        return instance;
    }

    public void registListener (MusicInfoReadyListener listener) {
        mMusicInfoReadyListener = listener;
    }

    public void init(Context context) {
        mContext = context;
        new DataTask().execute();
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
                setDataSource(MusicInfo.getDataSource(Command.AUTO_PLAY));
            }
        });
    }

    private final class DataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mMusicList = MusicInfo.crateMusicArray(mContext);
            initFirstPlayInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void str) {
            super.onPostExecute(str);
            mMusicInfoReadyListener.musicInfoReady(mMusicList);
        }
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
                setDataSource(MusicInfo.getDataSource(arg));
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

    public interface MusicInfoReadyListener{
        void musicInfoReady(String[][] musicInfo);
        void musicPlayingInfo(String title, String artist, String ablum);
    }

    private final void initFirstPlayInfo() {
        mPlayingMusicInfo = MusicInfo.getPlayingMusicInfo(mContext);
        mDataSourceArray = MusicInfo.getDataSourceArray();
        mMusicListLength = mMusicList[mDataSourceArray].length;
        if (mPlayingMusicInfo != null) {
            for (int i = 0; i < mMusicListLength; i++) {
                if (mPlayingMusicInfo.equals(mMusicList[mDataSourceArray][i])) {
                    mPlayMusicIndex = i;
                    return;
                }
            }
        } else {
            mPlayMusicIndex = 0;
            mPlayingMusicInfo = mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }
}
