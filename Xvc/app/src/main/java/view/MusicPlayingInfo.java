package view;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import codelala.xvc.CallBack;
import codelala.xvc.Command;
import codelala.xvc.MusicBinder;
import codelala.xvc.R;
import codelala.xvc.Utils;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class MusicPlayingInfo {

    private final int[] mRes = new int[]{R.id.play_title, R.id.play_album, R.id.play_artist, R.id.play_time};
    private final int mResLen = mRes.length;
    private SparseArray<TextView> mPlayTextArray = new SparseArray<>(mResLen);
    private MusicBinder mMusicBinder;

    public MusicPlayingInfo(View view, MusicBinder musicBinder) {
        init(view);
        mMusicBinder = musicBinder;
        mMusicBinder.sendMsg(mMusicPlayingInfo, Command.REGISTER_PLAYING_INFO);
    }

    private final CallBack.MusicPlayingInfo mMusicPlayingInfo = new CallBack.MusicPlayingInfo() {
        @Override
        public void musicPlayingInfo(String artist, String title, String ablum, int time) {
            mPlayTextArray.get(R.id.play_artist).setText(artist);
            mPlayTextArray.get(R.id.play_title).setText(title);
            mPlayTextArray.get(R.id.play_album).setText(ablum);
            mPlayTextArray.get(R.id.play_time).setText(Utils.formatMusicTime(time));
        }
    };

    private final void init(View view) {
        for (int i = 0; i < mResLen; i++) {
            TextView tmp = (TextView) view.findViewById(mRes[i]);
            mPlayTextArray.append(mRes[i], tmp);
        }
    }

}
