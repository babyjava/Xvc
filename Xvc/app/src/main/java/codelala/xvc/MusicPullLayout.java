package codelala.xvc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import codelala.xvc.MusicUtils;
import codelala.xvc.R;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
public final class MusicPullLayout extends FrameLayout {

    public MusicPullLayout(Context context) {
        super(context);
        init();
    }

    public MusicPullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_music_pull_layout, null);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        addView(view, lp);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MusicUtils.log("xxxx xxxx xxxx");
        return super.onTouchEvent(event);

    }
}
