package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import codelala.xvc.MusicCallBack;
import codelala.xvc.MusicCommand;
import codelala.xvc.MusicBinder;
import codelala.xvc.R;
import codelala.xvc.MusicUtils;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public final class MusicInfo {

    private final int[] mRes = new int[]{R.id.play_title, R.id.play_album, R.id.play_artist, R.id.play_time};
    private final int mResLen = mRes.length;
    private SparseArray<TextView> mPlayTextArray = new SparseArray<>(mResLen);
    private MusicBinder mMusicBinder;

    public MusicInfo(View view, int yLocation) {
        MusicUtils.setTranslationY(view, yLocation);
        findViewById(view);
    }

    public void setBinder(MusicBinder musicBinder) {
        if (musicBinder != null) {
            mMusicBinder = musicBinder;
            mMusicBinder.sendMsg(mMusicPlayingInfo, MusicCommand.REGISTER_PLAYING_INFO);
        }
    }

    private MusicCallBack.MusicPlayingInfo mMusicPlayingInfo = new MusicCallBack.MusicPlayingInfo() {
        @Override
        public void musicPlayingInfo(String artist, String title, String ablum, int time) {
            mPlayTextArray.get(R.id.play_artist).setText(artist);
            mPlayTextArray.get(R.id.play_title).setText(title);
            mPlayTextArray.get(R.id.play_album).setText(ablum);
            mPlayTextArray.get(R.id.play_time).setText(MusicUtils.formatMusicTime(time));
        }
    };

    private void findViewById(View view) {
        for (int i = 0; i < mResLen; i++) {
            TextView tmp = (TextView) view.findViewById(mRes[i]);
            mPlayTextArray.append(mRes[i], tmp);
        }
    }

}
