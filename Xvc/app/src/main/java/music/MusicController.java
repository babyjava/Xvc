package music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import codelala.xvc.Command;
import codelala.xvc.Utils;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class MusicController {

    private MediaPlayer mMediaPlayer  = new MediaPlayer();;
    private static MusicController instance = new MusicController();
    private boolean mIsPause = true;
    private boolean mIsInit = false;
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

    public interface MusicInfoReadyListener {
        void musicInfoReady(String[][] musicInfo);
        void musicPlayingInfo(String title, String artist, String ablum, int duration);
        void musicPlayingStatus(boolean isPlaying, int duration);
        void musicPlayingPosition(int position);
    }

    public void init(Context context) {
        mContext = context;
        new DataTask().execute();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                doMusicStart();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setDataSource(autoPlayDataSource());
            }
        });
    }

    public final void handleMsg(Message msg) {
        int what = (msg == null?Command.SYSTEM_ERROR:msg.what);
        switch (what) {
            case Command.CLICK_MODE_LOOP:
            case Command.CLICK_MODE_SINGLE:
            case Command.CLICK_MODE_RANDOM:
                mPlayMode = what;
                MusicInfo.savePlayMode(what, mContext);
                break;
            case Command.CLICK_PLAY_LAST:
            case Command.CLICK_PLAY_NEXT:
                setDataSource(clickGetDataSource(what));
                break;
            case Command.CLICK_PLAY_PLAY:
                doClickPlayAction(what);
                break;
            case Command.ACTIVITY_REGIST_LISTENER:
                registerListener(msg.obj);
                break;
            case Command.ACTIVITY_UNREGIST_LISTENER:
                unRegisterListener();
                break;
            case Command.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION:
                mMediaPlayer.seekTo(msg.arg1 > -1?msg.arg1:0);
                break;
        }
    }

    /*
    public ---------------------------up
    private --------------------------down
    * */

    private final void unRegisterListener() {
        mMusicInfoReadyListener = null;
    }

    private final void registerListener(Object obj) {
        if (obj != null) {
            mMusicInfoReadyListener = (MusicInfoReadyListener) obj;
        }
        callBackOfMusicInfoReady();
        if (mMediaPlayer.isPlaying()) {
            callBackOfPlayingInfo();
            callBackOfMusicPlayingStatus();
        }
    }

    private final void doClickPlayAction(int arg) {
        if (mMediaPlayer.isPlaying()) {
            doMusicPause();
        } else {
            if (mIsInit && mIsPause) {
                doMusicStart();
            } else {
                if (mMusicList == null) {
                } else {
                    setDataSource(clickGetDataSource(arg));
                }
            }
        }
    }

    private final void callBackOfMusicPlayingStatus() {
        if (mMusicInfoReadyListener != null) mMusicInfoReadyListener.musicPlayingStatus(mMediaPlayer.isPlaying(), getDuration());
    }

    private final void doMusicStart() {
        mIsInit = true;
        mMediaPlayer.start();
        mIsPause = false;
        callBackOfPlayingInfo();
        callBackOfMusicPlayingStatus();
    }

    private final void callBackOfMusicInfoReady() {
        Utils.toast(mContext, "callBackOfMusicInfoReady");
        if (mMusicInfoReadyListener != null && mMusicList != null) {
            mMusicInfoReadyListener.musicInfoReady(mMusicList);
        }
    }

    private final void callBackOfPlayingInfo() {
        if (mMusicInfoReadyListener != null && mMusicList != null) {
            mMusicInfoReadyListener.musicPlayingInfo(mMusicList[0][mPlayMusicIndex],
                    mMusicList[1][mPlayMusicIndex],
                    mMusicList[2][mPlayMusicIndex],
                    getDuration());
        }
    }

    private final int getDuration() {
        int d = mMediaPlayer.getDuration();
        return ((d < 0) ? 240 : d);
    }

    private final void doMusicPause() {
        mMediaPlayer.pause();
        mIsPause = true;
        callBackOfMusicPlayingStatus();
        MusicInfo.savePlayingMusicInfo(mContext, mMusicList[mDataSourceArray][mPlayMusicIndex]);
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
            callBackOfMusicInfoReady();
        }
    }

    private final void setDataSource(String dataSource) {
        mMediaPlayer.reset();
        mIsPause = false;
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

    private final void initFirstPlayInfo() {
        if (mMusicList == null) return;
        mPlayingMusicInfo = MusicInfo.getPlayingMusicInfo(mContext);
        mDataSourceArray = MusicInfo.getDataSourceArray();
        mMusicListLength = mMusicList[mDataSourceArray].length;
        mPlayMode = MusicInfo.getPlayMode(mContext);
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

    private final String clickGetDataSource(int arg) {
        switch (arg) {
            case Command.CLICK_PLAY_LAST:
                return mMusicList[mDataSourceArray][getLastIndex()];
            case Command.CLICK_PLAY_NEXT:
                return mMusicList[mDataSourceArray][getNextIndex()];
            case Command.CLICK_PLAY_PLAY:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    private final String autoPlayDataSource() {
        switch (mPlayMode) {
            case Command.CLICK_MODE_LOOP:
                return mMusicList[mDataSourceArray][getNextIndex()];
            case Command.CLICK_MODE_RANDOM:
                return mMusicList[mDataSourceArray][getRandomIndex()];
            case Command.CLICK_MODE_SINGLE:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    private final int getRandomIndex() {
        Random r = new Random();
        return r.nextInt(mMusicListLength);
    }

    private final int getLastIndex() {
        mPlayMusicIndex--;
        if (mPlayMusicIndex < 0) {
            mPlayMusicIndex = mMusicListLength - 1;
        }
        return mPlayMusicIndex;
    }

    private final int getNextIndex() {
        mPlayMusicIndex++;
        if (mPlayMusicIndex >= mMusicListLength) {
            mPlayMusicIndex = 0;
        }
        return mPlayMusicIndex;
    }
}
