package music;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Random;

import codelala.xvc.Command;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class MusicController {

    private MediaPlayer mMediaPlayer;
    private String[][] mMusicData;
    private static MusicController instance = new MusicController();
    private MusicInfo mMusicInfo = MusicInfo.getInstance();
    private boolean mPause;
    private String mPlayingMusicInfo;
    private int mPlayMode;
    private int mPlayMusicIndex;
    private int mDataSourceArray;
    private int mMusicInfoSize;

    private MusicController(){}

    public static MusicController getInstance() {
        return instance;
    }

    public void init(final Context context) {
        mMediaPlayer = new MediaPlayer();
        mMusicInfo.start(new MusicInfo.MusicInfoReadyListener() {
            @Override
            public void musicInfoReady(String[][] musicInfo) {
                if (musicInfo != null) return;
                mMusicData = musicInfo;
                mPlayMode = context.getSharedPreferences(Command.MUSIC_PLAY_MODE, 0).getInt(Command.MUSIC_PLAY_MODE, Command.CLICK_MODE_LOOP);
                mPlayingMusicInfo = context.getSharedPreferences(Command.MUSIC_RECORD, 0).getString(Command.MUSIC_RECORD, null);
                mDataSourceArray = mMusicInfo.getDataSourceIndex();
                mMusicInfoSize = mMusicData[mDataSourceArray].length;
                if (mPlayingMusicInfo == null) {
                    mPlayMusicIndex = 0;
                    mPlayingMusicInfo = mMusicData[mDataSourceArray][mPlayMusicIndex];
                } else {
                    boolean available = false;
                    for (int i = 0; i < mMusicInfoSize; i++) {
                        if (mPlayingMusicInfo.equals(mMusicData[mDataSourceArray][i])) {
                            mPlayMusicIndex = i;
                            available = true;
                            break;
                        }
                    }
                    if (!available) {
                        mPlayMusicIndex = 0;
                        mPlayingMusicInfo = mMusicData[mDataSourceArray][mPlayMusicIndex];
                    }
                }

            }
        }, context);
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

    public void play(int arg) {
        switch (arg) {
            case Command.CLICK_LAST:
                musicPlayLast();
                break;
            case Command.CLICK_NEXT:
                musicPlayNext();
                break;
            case Command.CLICK_PLAY:
                musicPlay();
                break;
            default:
                break;
        }
    }

    private final void musicAutoPlay() {
        switch (mPlayMode) {
            case Command.CLICK_MODE_LOOP:
                musicPlayNext();
                break;
            case Command.CLICK_MODE_RANDOM:
                musicPlayRandom();
                break;
            case Command.CLICK_MODE_SINGLE:
                musicStart();
                break;
            default:
                break;
        }
    }

    private final void musicPlayRandom() {
        Random r = new Random();
        mPlayMusicIndex = r.nextInt(mMusicInfoSize);
        musicStart();
    }

    private final void musicPlayNext() {
        mPlayMusicIndex++;
        if (mPlayMusicIndex >= mMusicInfoSize) {
            mPlayMusicIndex = 0;
        }
        musicStart();
    }

    private final void musicPlayLast() {
        mPlayMusicIndex--;
        if (mPlayMusicIndex < 0) {
            mPlayMusicIndex = mMusicInfoSize - 1;
        }
        musicStart();
    }

    private final void musicPlay() {
        if (mMusicData == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPause = true;
        } else {
            if (mPause) {
                mMediaPlayer.start();
            } else {
                musicStart();
            }
            mPause = false;
        }

    }

    private final void musicStart() {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mMusicData[mDataSourceArray][mPlayMusicIndex]);
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
