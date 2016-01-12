package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends Activity{

    private ServiceConnection mServiceConnection;
    private MusicPlayService.MusicBinder mMusicBinder;
    private ImageView mPlayView;
    private SeekBar mMusicSeekBar;
    private SparseArray<ImageView> mPlayViewArry = new SparseArray<>();

    public music.MusicController.MusicInfoReadyListener mListener = new music.MusicController.MusicInfoReadyListener() {

        @Override
        public void musicInfoReady(String[][] musicInfo) {
            Utils.toast(MainActivity.this, "musicInfoReady");
        }

        @Override
        public void musicPlayingInfo(String title, String artist, String ablum, int duration) {
        }

        @Override
        public void musicPlayingStatus(boolean isPlaying) {
            mPlayView.setImageResource(isPlaying?R.drawable.playing:R.drawable.pause);
        }
        @Override
        public void musicPlayingPosition(int position) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnect();
        init();
    }

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

    private final View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            doOnClick(v == null?Command.SYSTEM_ERROR:v.getId());
        }
    };

    private final void init() {
        int[] playViewId = new int[]{R.id.play_play, R.id.play_next, R.id.play_last};
        int len = playViewId.length;
        for (int i = 0; i < len; i++) {
            ImageView tmp = (ImageView) findViewById(playViewId[i]);
            tmp.setOnClickListener(mClickListener);
            mPlayViewArry.append(playViewId[i], tmp);
        }
        mMusicSeekBar = (SeekBar) findViewById(R.id.play_seekbar);
        mMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //dragseekbar = false;
                mMusicBinder.sendMsg(Command.TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION, seekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //dragseekbar=true;
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private final void serviceConnect() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Utils.toast(MainActivity.this, "onServiceConnected");
                mMusicBinder = (MusicPlayService.MusicBinder) service;
                mMusicBinder.sendMsg(mListener, Command.ACTIVITY_REGIST_LISTENER);
            }
        };
        bindService(new Intent(MainActivity.this, MusicPlayService.class), mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicBinder.sendMsg(Command.ACTIVITY_UNREGIST_LISTENER);
        unbindService(mServiceConnection);
        mMusicBinder = null;
        mServiceConnection = null;
    }

}
