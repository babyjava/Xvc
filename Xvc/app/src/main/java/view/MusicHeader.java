package view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import codelala.xvc.MusicBinder;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class MusicHeader {

    private View mHeadView;
    private MusicBinder mMusicBinder;

    public MusicHeader(View view) {
        mHeadView = view;
    }

    public void setBinder(MusicBinder musicBinder) {
        if (mMusicBinder == null && musicBinder != null) {
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


//    public View getHeadView() {
//
//    }
}
