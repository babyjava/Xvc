package codelala.xvc;

import android.app.Activity;
import android.os.Bundle;

import music.MusicInfo;

public class MainActivity extends Activity implements MusicInfo.MusicInfoReadyListener{

    private MusicInfo mMusicInfo = MusicInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMusicInfo.start(this, this);
    }

    @Override
    public void musicInfoReady(String[][] musicInfo) {
        init();
    }

    @Override
    public void musicPlayingInfo(String title, String artist, String ablum) {

    }

    private final void init() {

    }
}
