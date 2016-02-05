package codelala.xvc;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Message;

import java.io.IOException;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public final class MusicController {

    private MediaPlayer mMediaPlayer;
    private static MusicController instance;
    private boolean mIsPause = true;
    private boolean mIsInit = false;
    private boolean mIsReady = false;
    private String[][] mMusicList;
    private Context mContext;
    private MusicCallBack.MusicPlayBarListener mMusicPlayBarListener;
    private MusicCallBack.MusicPlayingInfo mMusicPlayingInfo;
    private MusicCallBack.MusicSeekBarStatus mMusicSeekBarStatus;
    private MusicCallBack.MusicInfoReady mMusicInfoReady;
    private String mPlayingMusicInfo;
    private int mPlayMode;
    private int mDataSourceArray;
    private int mPlayMusicIndex;
    private int mMusicListLength;

    private void destroy() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        instance = null;
    }

    private MusicController(){}

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        mMediaPlayer  = new MediaPlayer();
        new DataTask().execute();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsReady = true;
                mIsPause = true;
                doMusicStart();
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mIsReady = false;
                setDataSource(autoPlayDataSource());
            }
        });
    }

    public void handleMsg(Message msg) {
        int what = (msg == null? MusicCommand.SYSTEM_ERROR:msg.what);
        switch (what) {
            case MusicCommand.MUSIC_MAIN_ACTIVITY_DESTROY:
                destroy();
                break;
            case MusicCommand.CLICK_MODE_LOOP:
            case MusicCommand.CLICK_MODE_SINGLE:
            case MusicCommand.CLICK_MODE_RANDOM:
                mPlayMode = what;
                MusicData.savePlayMode(what, mContext);
                break;
            case MusicCommand.CLICK_PLAY_LAST:
            case MusicCommand.CLICK_PLAY_NEXT:
                setDataSource(clickGetDataSource(what));
                break;
            case MusicCommand.CLICK_PLAY_PLAY:
                doClickPlayAction();
                break;
            case MusicCommand.REGISTER_MUSIC_PLAY_BAR_LISTENER:
                callBackOfMusicPlayBarStatus(msg.obj);
                break;
            case MusicCommand.REGISTER_PLAYING_INFO:
                callBackOfMusicPlayingInfo(msg.obj);
                break;
            case MusicCommand.REGISTER_MUSIC_ARRAY_READY_LISTENER:
                callBackOfMusicInfoReady(msg.obj);
                break;
            case MusicCommand.REGISTER_SEEKBAR_STATUS:
                callBackOfMusicSeekBarStatus(msg.obj);
                break;
            case MusicCommand.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION:
                seekTo(msg.arg1 < 0?0:msg.arg1);
                break;
        }
    }

    public boolean isPlaying() {
        return !mIsPause;
    }
    /*
    public ---------------------------up
    private --------------------------down
    * */

    private void seekTo(int position) {
        if (mMusicList == null) {

        } else {
            mMediaPlayer.seekTo(position);
        }
    }

    private void callBackOfMusicSeekBarStatus(Object obj) {
        if (mMusicSeekBarStatus == null && obj != null) {
            mMusicSeekBarStatus = (MusicCallBack.MusicSeekBarStatus) obj;
        }
        if (mMusicSeekBarStatus != null && mMusicList != null) {
            mMusicSeekBarStatus.musicSeekBarStatus(isPlaying(), mMediaPlayer.getCurrentPosition(), getDuration());
        }
    }

    private void doClickPlayAction() {
        mIsInit = true;
        if (mIsPause) {
            doMusicStart();
        } else {
           doMusicPause();
        }
    }

    private void callBackOfMusicPlayBarStatus(Object obj) {
        if (obj != null) {
            mMusicPlayBarListener = (MusicCallBack.MusicPlayBarListener) obj;
        }
        if (mMusicPlayBarListener != null) {
            mMusicPlayBarListener.musicPlayBarStatus(isPlaying());
        }
    }

    private void doMusicStart() {
        if (mIsReady && mIsInit) {
            mMediaPlayer.start();
            mIsPause = false;
            callBackOfMusicPlayingInfo(null);
            callBackOfMusicPlayBarStatus(null);
        }
    }

    private void callBackOfMusicInfoReady(Object obj) {
        if (obj != null) {
            mMusicInfoReady = (MusicCallBack.MusicInfoReady) obj;
        }
        if (mMusicInfoReady != null) {
            mMusicInfoReady.musicInfoReady(mMusicList);
        }
    }

    private void callBackOfMusicPlayingInfo(Object obj) {
        if (mMusicPlayingInfo == null && obj != null) {
            mMusicPlayingInfo = (MusicCallBack.MusicPlayingInfo) obj;
        }
        if (mMusicPlayingInfo != null && mMusicList != null) {
            mMusicPlayingInfo.musicPlayingInfo(mMusicList[0][mPlayMusicIndex],
                    mMusicList[1][mPlayMusicIndex],
                    mMusicList[2][mPlayMusicIndex],
                    getDuration());
        }
    }

    private int getDuration() {
        int d = mMediaPlayer.getDuration();
        return ((d < 0) ? 240 : d);
    }

    private void doMusicPause() {
        mMediaPlayer.pause();
        mIsPause = true;
        callBackOfMusicPlayBarStatus(null);
        MusicData.savePlayingMusicInfo(mContext, mMusicList[mDataSourceArray][mPlayMusicIndex]);
    }

    private final class DataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mMusicList = MusicData.crateMusicArray(mContext);
            initFirstPlayInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void str) {
            super.onPostExecute(str);
            callBackOfMusicInfoReady(null);
            MusicUtils.log("mPlayingMusicInfo= " + mPlayingMusicInfo);
            setDataSource(mPlayingMusicInfo);
        }
    }

    private void setDataSource(String dataSource) {
        if (dataSource == null) return;
        mMediaPlayer.reset();
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

    private void initFirstPlayInfo() {
        if (mMusicList == null) return;
        mPlayingMusicInfo = MusicData.getPlayingMusicInfo(mContext);
        mDataSourceArray = MusicData.getDataSourceArray();
        mMusicListLength = mMusicList[mDataSourceArray].length;
        mPlayMode = MusicData.getPlayMode(mContext);
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

    private String clickGetDataSource(int arg) {
        if (mMusicList == null) return null;
        switch (arg) {
            case MusicCommand.CLICK_PLAY_LAST:
                return mMusicList[mDataSourceArray][MusicUtils.getLastIndex(mPlayMusicIndex, mMusicListLength)];
            case MusicCommand.CLICK_PLAY_NEXT:
                return mMusicList[mDataSourceArray][MusicUtils.getNextIndex(mPlayMusicIndex, mMusicListLength)];
            case MusicCommand.CLICK_PLAY_PLAY:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    private String autoPlayDataSource() {
        if (mMusicList == null) return null;
        switch (mPlayMode) {
            case MusicCommand.CLICK_MODE_LOOP:
                return mMusicList[mDataSourceArray][MusicUtils.getNextIndex(mPlayMusicIndex, mMusicListLength)];
            case MusicCommand.CLICK_MODE_RANDOM:
                return mMusicList[mDataSourceArray][MusicUtils.getRandomIndex(mMusicListLength)];
            case MusicCommand.CLICK_MODE_SINGLE:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }


}
