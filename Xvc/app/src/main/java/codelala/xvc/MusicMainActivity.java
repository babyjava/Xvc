package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

import view.MusicHeaderBar;
import view.MusicPlayBar;
import view.MusicTitleBar;
import view.MusicSeekBar;

public final class MusicMainActivity extends Activity {

    private ServiceConnection mServiceConnection;
    private MusicPlayService.MusicBinder mMusicBinder;
    private MusicPlayBar mMusicPlayBar;
    private MusicSeekBar mMusicSeekBar;
    private MusicTitleBar mMusicTitleBar;
    private MusicHeaderBar mMusicHeaderBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        serviceConnect();
    }

    private void setYLocation() {
        int y = MusicUtils.getScreenHeight(this);
        y = mMusicPlayBar.setLocation(y);
        y = mMusicSeekBar.setLocation(y);
        y = mMusicTitleBar.setLocation(y);
        mMusicHeaderBar.setLocation(y);
    }

    private void findViewById() {
        mMusicHeaderBar = new MusicHeaderBar(findViewById(R.id.music_head_bar));
        mMusicPlayBar = new MusicPlayBar(findViewById(R.id.music_play_bar));
        mMusicSeekBar = new MusicSeekBar(findViewById(R.id.music_seek_bar));
        mMusicTitleBar = new MusicTitleBar(findViewById(R.id.music_title_bar));
    }

    private void initBinder(IBinder service) {
        mMusicBinder = (MusicPlayService.MusicBinder) service;
        mMusicPlayBar.setBinder(mMusicBinder);
        mMusicTitleBar.setBinder(mMusicBinder);
        mMusicSeekBar.setBinder(mMusicBinder);
        mMusicHeaderBar.setBinder(mMusicBinder);
    }

    private void serviceConnect() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                initBinder(service);
                setYLocation();
            }
        };
        bindService(new Intent(MusicMainActivity.this, MusicPlayService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMusicPlayBar.isPlaying()) {
            unbindService(mServiceConnection);
        } else {
            mMusicBinder.sendMsg(MusicCommand.MUSIC_MAIN_ACTIVITY_DESTROY);
            stopService(new Intent(this, MusicPlayService.class));
        }
    }
}
