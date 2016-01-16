package view;

import android.view.View;
import android.widget.SeekBar;
import codelala.xvc.CallBack;
import codelala.xvc.Command;
import codelala.xvc.MusicBinder;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class MusicSeekBar {

    private MusicBinder mMusicBinder;
    private SeekBar mMusicSeekBar;
    private boolean mIsPlaying;
    private boolean mIsTouchSeekBar;

    public MusicSeekBar(View view, MusicBinder musicBinder) {
        init(view);
        mMusicBinder = musicBinder;
        mMusicBinder.sendMsg(mMusicSeekBarStatus, Command.REGISTER_SEEKBAR_STATUS);
    }

    private final CallBack.MusicSeekBarStatus mMusicSeekBarStatus = new CallBack.MusicSeekBarStatus() {

        @Override
        public void musicSeekBarStatus(boolean isPlaying, int position, int time) {
            mIsPlaying = isPlaying;
            runSeekBar(time, position);
        }
    };

    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            mMusicSeekBar.postDelayed(this, 100);
            if (mIsTouchSeekBar) return;
            int position = 100 + mMusicSeekBar.getProgress();
            if (!mIsPlaying || position > mMusicSeekBar.getMax()) {
                mMusicSeekBar.removeCallbacks(this);
                return;
            }
            mMusicSeekBar.setProgress(position);
        }
    };

    private final void init(View view) {
        mMusicSeekBar = (SeekBar) view;
        mMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsTouchSeekBar = false;
                mMusicBinder.sendMsg(Command.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION, seekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsTouchSeekBar = true;
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private final void runSeekBar(int duration, int position) {
        mMusicSeekBar.removeCallbacks(r);
        mMusicSeekBar.setMax(duration);
        mMusicSeekBar.setProgress(position);
        mMusicSeekBar.postDelayed(r, 100);
    }

}
