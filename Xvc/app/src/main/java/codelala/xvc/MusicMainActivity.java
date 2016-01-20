package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

import view.MusicBody;
import view.MusicBar;
import view.MusicInfo;
import view.MusicSeekBar;

public class MusicMainActivity extends Activity {

    private ServiceConnection mServiceConnection;
    private MusicBinder mMusicBinder;
    private MusicBar mMusicBar;
    private MusicInfo mMusicInfo;
    private MusicSeekBar mMusicSeekBar;
    private MusicBody mMusicBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnect();
    }

    private final MusicCallBack.MusicInfoReady mMusicInfoReady = new MusicCallBack.MusicInfoReady() {
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

    private final void init() {
        if (mMusicBar == null) {
            mMusicBar = (MusicBar) findViewById(R.id.play_bar);
            mMusicBar.setBinder(mMusicBinder);
            mMusicInfo = (MusicInfo) findViewById(R.id.play_info);
            mMusicInfo.setBinder(mMusicBinder);
            mMusicSeekBar = (MusicSeekBar) findViewById(R.id.play_seekbar);
            mMusicSeekBar.setBinder(mMusicBinder);
            mMusicBody = (MusicBody) findViewById(R.id.play_body);
            mMusicBody.setBinder(mMusicBinder);
        }
    }

    private final void serviceConnect() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicUtils.toast(MusicMainActivity.this, "onServiceConnected");
                mMusicBinder = (MusicBinder) service;
                mMusicBinder.sendMsg(mMusicInfoReady, MusicCommand.REGISTER_INFO_READY);
                init();
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

//    private final static class MyAdapter extends FragmentPagerAdapter {
//        ArrayList<Fragment> mList;
//        Test mMusicListFragment;
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//            mList = new ArrayList<>(2);
//            mMusicListFragment = new Test();
//            mList.add(mMusicListFragment);
//            mList.add(new MusicLyricFragment());
//        }
//
//        public void updateFragment(String[][] musicInfo) {
//            mMusicListFragment.setArguments(musicInfo);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mList == null?0:mList.size();
//        }
//    }

}
