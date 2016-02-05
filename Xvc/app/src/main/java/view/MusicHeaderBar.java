package view;

import android.view.View;
import codelala.xvc.MusicPlayService.MusicBinder;
import codelala.xvc.MusicCommand;
import codelala.xvc.MusicUtils;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public final class MusicHeaderBar extends BaseView{

    private View mView;
    private MusicBinder mMusicBinder;

    public MusicHeaderBar(View view) {
        mView = view;
    }

    @Override
    public void setBinder(MusicBinder musicBinder) {
            mMusicBinder = musicBinder;
    }

    @Override
    public int setLocation(int y) {
        MusicUtils.setPivotY(mView, 0.0f);
        MusicUtils.setScaleY(mView, (float)y/mView.getHeight());
        return 0;
    }


    private final View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doOnClick(v.getId());
        }
    };

//    private final MusicCallBack.MusicPlayingStatus mMusicPlayingStatus = new MusicCallBack.MusicPlayingStatus() {
//        @Override
//        public void musicPlayingStatus(boolean isPlaying) {
//        }
//    };

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
