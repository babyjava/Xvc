package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class MusicBody extends RelativeLayout {
    private MusicBinder mMusicBinder;

    public MusicBody(Context context) {
        super(context);
        init(context);
    }

    public void setBinder(MusicBinder musicBinder) {
        if (mMusicBinder == null) {
            mMusicBinder = musicBinder;
            mMusicBinder.sendMsg(null, MusicCommand.REGISTER_PLAYING_STATUS);
        }
    }

    public MusicBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private final void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.play_music_body, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(view, lp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
