package view;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import codelala.xvc.CallBack;
import codelala.xvc.Command;
import codelala.xvc.MusicBinder;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class MusicPlayBar {
    private final int[] mRes = new int[]{R.id.play_play, R.id.play_next, R.id.play_last};
    private final int mResLen = mRes.length;
    private SparseArray<ImageView> mPlayViewArray = new SparseArray<>(mResLen);
    private MusicBinder mMusicBinder;

    public MusicPlayBar(View view, MusicBinder musicBinder) {
        init(view);
        mMusicBinder = musicBinder;
        mMusicBinder.sendMsg(mMusicPlayingStatus, Command.REGISTER_PLAYING_STATUS);
    }

    private CallBack.MusicPlayingStatus mMusicPlayingStatus = new CallBack.MusicPlayingStatus() {
        @Override
        public void musicPlayingStatus(boolean isPlaying) {
            if (mPlayViewArray == null) return;
            mPlayViewArray.get(R.id.play_play).setImageResource(isPlaying ? R.drawable.playing : R.drawable.pause);
        }
    };

    private final View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doOnClick(v == null? Command.SYSTEM_ERROR:v.getId());
        }
    };

    private final void doOnClick(int id) {
        if (mMusicBinder == null) return;
        switch (id) {
            case R.id.play_last:
                mMusicBinder.sendMsg(Command.CLICK_PLAY_LAST);
                break;
            case R.id.play_next:
                mMusicBinder.sendMsg(Command.CLICK_PLAY_NEXT);
                break;
            case R.id.play_play:
                mMusicBinder.sendMsg(Command.CLICK_PLAY_PLAY);
                break;
        }
    }

    private final void init(View view) {
        for (int i = 0; i < mResLen; i++) {
            ImageView tmp = (ImageView) view.findViewById(mRes[i]);
            tmp.setOnClickListener(mClickListener);
            mPlayViewArray.append(mRes[i], tmp);
        }
    }

}
