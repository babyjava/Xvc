package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import codelala.xvc.MusicBinder;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public class MusicBar extends RelativeLayout {
    private final int[] mRes = new int[]{R.id.play_play, R.id.play_next, R.id.play_last};
    private final int mResLen = mRes.length;
    private final SparseArray<ImageView> mPlayViewArray = new SparseArray<>(mResLen);
    private MusicBinder mMusicBinder;

    public MusicBar(Context context) {
        super(context);
        init(context);
    }

    public void setBinder(MusicBinder musicBinder) {
        if (mMusicBinder == null) {
            mMusicBinder = musicBinder;
            mMusicBinder.sendMsg(mMusicPlayingStatus, MusicCommand.REGISTER_PLAYING_STATUS);
        }
    }

    public MusicBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
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
            mPlayViewArray.get(R.id.play_play).setImageResource(isPlaying ? R.drawable.playing : R.drawable.pause);
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

    private final void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_music_bar, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, lp);
        for (int i = 0; i < mResLen; i++) {
            ImageView tmp = (ImageView) view.findViewById(mRes[i]);
            tmp.setOnClickListener(mClickListener);
            mPlayViewArray.append(mRes[i], tmp);
        }
    }
}
