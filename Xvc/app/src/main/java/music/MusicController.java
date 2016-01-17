package music;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import codelala.xvc.CallBack;
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
    private CallBack.MusicPlayingStatus mMusicPlayingStatus;
    private CallBack.MusicPlayingInfo mMusicPlayingInfo;
    private CallBack.MusicSeekBarStatus mMusicSeekBarStatus;
    private CallBack.MusicInfoReady mMusicInfoReady;
    private String mPlayingMusicInfo;
    private int mPlayMode;
    private int mDataSourceArray;
    private int mPlayMusicIndex;
    private int mMusicListLength;

    private MusicController(){}

    public static MusicController getInstance() {
        return instance;
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
            case Command.REGISTER_PLAYING_STATUS:
                mMusicPlayingStatus = (CallBack.MusicPlayingStatus) msg.obj;
                break;
            case Command.REGISTER_PLAYING_INFO:
                callBackOfMusicPlayingInfo(msg.obj);
                break;
            case Command.REGISTER_INFO_READY:
                callBackOfMusicInfoReady(msg.obj);
                break;
            case Command.REGISTER_SEEKBAR_STATUS:
                callBackOfMusicSeekBarStatus(msg.obj);
                break;
            case Command.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION:
                seekTo(msg.arg1 < 0?0:msg.arg1);
                break;
        }
    }

    /*
    public ---------------------------up
    private --------------------------down
    * */

    private final void seekTo(int position) {
        if (mMusicList == null) {

        } else {
            mMediaPlayer.seekTo(position);
        }
    }

    private final void callBackOfMusicSeekBarStatus(Object obj) {
        if (mMusicSeekBarStatus == null && obj != null) {
            mMusicSeekBarStatus = (CallBack.MusicSeekBarStatus) obj;
        }
        if (mMusicSeekBarStatus != null && mMusicList != null) {
            mMusicSeekBarStatus.musicSeekBarStatus(isPlaying(), mMediaPlayer.getCurrentPosition(), getDuration());
        }
    }

    private final void doClickPlayAction(int arg) {
        if (isPlaying()) {
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

    private final boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    private final void callBackOfMusicPlayingStatus() {
        if (mMusicPlayingStatus != null) mMusicPlayingStatus.musicPlayingStatus(isPlaying());
    }

    private final void doMusicStart() {
        mIsInit = true;
        mMediaPlayer.start();
        mIsPause = false;
        callBackOfMusicPlayingInfo(null);
        callBackOfMusicPlayingStatus();
    }

    private final void callBackOfMusicInfoReady(Object obj) {
        if (mMusicInfoReady == null && obj != null) {
            mMusicInfoReady = (CallBack.MusicInfoReady) obj;
        }
        if (mMusicInfoReady != null/* && mMusicList != null*/) {
            mMusicInfoReady.musicInfoReady(mMusicList);
        }
    }

    private final void callBackOfMusicPlayingInfo(Object obj) {
        if (mMusicPlayingInfo == null && obj != null) {
            mMusicPlayingInfo = (CallBack.MusicPlayingInfo) obj;
        }
        if (mMusicPlayingInfo != null && mMusicList != null) {
            mMusicPlayingInfo.musicPlayingInfo(mMusicList[0][mPlayMusicIndex],
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
            callBackOfMusicInfoReady(null);
        }
    }

    private final void setDataSource(String dataSource) {
        if (dataSource == null) return;
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
        if (mMusicList == null) return null;
        switch (arg) {
            case Command.CLICK_PLAY_LAST:
                return mMusicList[mDataSourceArray][Utils.getLastIndex(mPlayMusicIndex, mMusicListLength)];
            case Command.CLICK_PLAY_NEXT:
                return mMusicList[mDataSourceArray][Utils.getNextIndex(mPlayMusicIndex, mMusicListLength)];
            case Command.CLICK_PLAY_PLAY:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    private final String autoPlayDataSource() {
        if (mMusicList == null) return null;
        switch (mPlayMode) {
            case Command.CLICK_MODE_LOOP:
                return mMusicList[mDataSourceArray][Utils.getNextIndex(mPlayMusicIndex, mMusicListLength)];
            case Command.CLICK_MODE_RANDOM:
                return mMusicList[mDataSourceArray][Utils.getRandomIndex(mMusicListLength)];
            case Command.CLICK_MODE_SINGLE:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }


}
