package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;

import view.MusicPlayBar;
import view.MusicTitleBar;
import view.MusicSeekBar;

public final class MusicMainActivity extends Activity {

    private ServiceConnection mServiceConnection;
    private MusicBinder mMusicBinder;
    private MusicPlayBar mMusicPlayBar;
    private MusicSeekBar mMusicSeekBar;
    private MusicTitleBar mMusicTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        serviceConnect();
    }

    private MusicCallBack.MusicInfoReady mMusicInfoReady = new MusicCallBack.MusicInfoReady() {
        @Override
        public void musicInfoReady(String[][] musicInfo) {
            MusicUtils.toast(MusicMainActivity.this, "musicInfoReady");
            if(musicInfo == null) {

            } else {
//                if (mViewPager == null) {
//                    mViewPager = (ViewPager) findViewById(R.id.play_viewpager);
//                    mMyAdapter = new MyAdapter(getSupportFragmentManager());
//                    mViewPager.setAdapter(mMyAdapter);
//                }
//                mMyAdapter.updateFragment(musicInfo);
            }
        }
    };

    private void setYLocation() {
        int y = MusicUtils.getScreenHeight(this);
        y = mMusicPlayBar.setYLocation(y);
        y = mMusicSeekBar.setYLocation(y);
        mMusicTitleBar.setYLocation(y);
    }

    private void findViewById() {
        mMusicPlayBar = new MusicPlayBar(findViewById(R.id.music_play_bar));
        mMusicSeekBar = new MusicSeekBar(findViewById(R.id.music_seek_bar));
        mMusicTitleBar = new MusicTitleBar(findViewById(R.id.music_title_bar));
    }

    private void initBinder(IBinder service) {
        mMusicBinder = (MusicBinder) service;
        mMusicPlayBar.setBinder(mMusicBinder);
        mMusicTitleBar.setBinder(mMusicBinder);
        mMusicSeekBar.setBinder(mMusicBinder);
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
        unbindService(mServiceConnection);
    }
}
