package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import view.MusicPlayBar;
import view.MusicPlayingInfo;
import view.MusicSeekBar;

public class MainActivity extends Activity{

    private ServiceConnection mServiceConnection;
    private MusicBinder mMusicBinder;
    private MusicPlayBar mMusicPlayBar;
    private MusicPlayingInfo mMusicPlayingInfo;
    private MusicSeekBar mMusicSeekBar;

    private final void showMusicList(String[][] musicInfo) {
        if(musicInfo == null) {

        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnect();
    }

    private final CallBack.MusicInfoReady mMusicInfoReady = new CallBack.MusicInfoReady() {
        @Override
        public void musicInfoReady(String[][] musicInfo) {
            Utils.toast(MainActivity.this, "musicInfoReady");
            showMusicList(musicInfo);
        }
    };

    private final void init() {
        if (mMusicPlayBar == null) {
            mMusicPlayBar = new MusicPlayBar(findViewById(R.id.play_bar), mMusicBinder);
            mMusicPlayingInfo = new MusicPlayingInfo(findViewById(R.id.play_info), mMusicBinder);
            mMusicSeekBar = new MusicSeekBar(findViewById(R.id.play_seekbar), mMusicBinder);
        }
    }

    private final void serviceConnect() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Utils.toast(MainActivity.this, "onServiceConnected");
                mMusicBinder = (MusicBinder) service;
                mMusicBinder.sendMsg(mMusicInfoReady, Command.REGISTER_INFO_READY);
                init();
            }
        };
        bindService(new Intent(MainActivity.this, MusicPlayService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

}
