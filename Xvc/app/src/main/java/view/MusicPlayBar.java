package view;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import codelala.xvc.MusicBinder;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.MusicUtils;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public final class MusicPlayBar extends BaseView {
    private final int[] mRes = new int[]{R.id.play_play, R.id.play_next, R.id.play_last};
    private final int mResLen = mRes.length;
    private final SparseArray<ImageView> mPlayViewArray = new SparseArray<>(mResLen);
    private MusicBinder mMusicBinder;
    private View mView;

    public MusicPlayBar(View view) {
        mView = view;
        findViewById();
    }

    @Override
    public void setBinder(MusicBinder musicBinder) {
        mMusicBinder = musicBinder;
        mMusicBinder.sendMsg(mMusicPlayBarListener, MusicCommand.REGISTER_MUSIC_PLAY_BAR_LISTENER);
    }

    @Override
    public int setYLocation(int yLocation) {
        return MusicUtils.setYLocation(mView, yLocation);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doOnClick(v.getId());
        }
    };

    private MusicCallBack.MusicPlayBarListener mMusicPlayBarListener = new MusicCallBack.MusicPlayBarListener() {
        @Override
        public void musicPlayBarStatus(boolean isPlaying) {
            mPlayViewArray.get(R.id.play_play).setImageResource(isPlaying ? R.drawable.playing : R.drawable.pause);
        }
    };

    private void doOnClick(int id) {
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

    private void findViewById() {
        for (int i = 0; i < mResLen; i++) {
            ImageView tmp = (ImageView) mView.findViewById(mRes[i]);
            tmp.setOnClickListener(mClickListener);
            mPlayViewArray.append(mRes[i], tmp);
        }
    }
}
