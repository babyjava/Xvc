package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.MusicBinder;
import codelala.xvc.MusicUtils;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public final class MusicSeekBar extends BaseView{

    private MusicBinder mMusicBinder;
    private SeekBar mMusicSeekBar;
    private boolean mIsPlaying;
    private boolean mIsTouchSeekBar;
    private View mView;

    public MusicSeekBar(View view) {
        mView = view;
        findViewById();
    }

    @Override
    public void setBinder(MusicBinder musicBinder) {
        mMusicBinder = musicBinder;
        mMusicBinder.sendMsg(mMusicSeekBarStatus, MusicCommand.REGISTER_SEEKBAR_STATUS);
    }

    @Override
    public int setYLocation(int yLocation) {
        return MusicUtils.setYLocation(mView, yLocation);
    }

    private MusicCallBack.MusicSeekBarStatus mMusicSeekBarStatus = new MusicCallBack.MusicSeekBarStatus() {

        @Override
        public void musicSeekBarStatus(boolean isPlaying, int position, int time) {
            mIsPlaying = isPlaying;
            runSeekBar(time, position);
        }
    };

    private Runnable r = new Runnable() {
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

    private void findViewById() {
        mMusicSeekBar = (SeekBar) mView.findViewById(R.id.play_seek_bar);
        mMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsTouchSeekBar = false;
                mMusicBinder.sendMsg(MusicCommand.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION, seekBar.getProgress());
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

    private void runSeekBar(int duration, int position) {
        mMusicSeekBar.removeCallbacks(r);
        mMusicSeekBar.setMax(duration);
        mMusicSeekBar.setProgress(position);
        mMusicSeekBar.postDelayed(r, 100);
    }

}
