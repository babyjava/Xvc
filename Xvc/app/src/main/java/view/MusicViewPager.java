package view;

import android.support.v4.view.ViewPager;
import android.view.View;
import codelala.xvc.MusicBinder;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class MusicViewPager {
    
    private MusicBinder mMusicBinder;
    private ViewPager mViewPager;

    public MusicViewPager(View vp) {
        mViewPager = (ViewPager) vp;
    }

    public void setBinder(MusicBinder musicBinder) {
        if (musicBinder != null) {
            mMusicBinder = musicBinder;
            mMusicBinder.sendMsg(mMusicPlayingStatus, MusicCommand.REGISTER_PLAYING_STATUS);
        }
    }

    private final View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doOnClick(v.getId());
        }
    };

    private final MusicCallBack.MusicPlayingStatus mMusicPlayingStatus = new MusicCallBack.MusicPlayingStatus() {
        @Override
        public void musicPlayingStatus(boolean isPlaying) {
        }
    };

    private final void doOnClick(int id) {
        if (mMusicBinder == null) return;
        switch (id) {
            case R.id.play_last:
                mMusicBinder.sendMsg(MusicCommand.CLICK_PLAY_LAST);
                break;
            case R.id.play_next:
                mMusicBinder.sendMsg(MusicCommand.CLICK_PLAY_NEXT);
                break;
            case R.id.play_play:
                mMusicBinder.sendMsg(MusicCommand.CLICK_PLAY_PLAY);
                break;
        }
    }

}
