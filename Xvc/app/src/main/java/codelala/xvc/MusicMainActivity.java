package codelala.xvc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;

import view.MusicHeader;
import view.MusicPullLayout;
import view.MusicBar;
import view.MusicInfo;
import view.MusicSeekBar;
import view.MusicViewPager;

public final class MusicMainActivity extends Activity {

    private ServiceConnection mServiceConnection;
    private MusicBinder mMusicBinder;
    private MusicBar mMusicBar;
    private MusicSeekBar mMusicSeekBar;
    private MusicInfo mMusicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void findViewById() {
        int yScreen = MusicUtils.getScreenHeight(this);
        View view = findViewById(R.id.view_music_bar);
        yScreen -= view.getHeight();
        MusicUtils.log("xieyan3 = " + view.getHeight());
        mMusicBar = new MusicBar(view, yScreen);
        view = findViewById(R.id.view_music_seekbar);
        yScreen -= view.getHeight();
        mMusicSeekBar = new MusicSeekBar(view, yScreen);
        view  = findViewById(R.id.view_music_info);
        yScreen -= view.getHeight();
        mMusicInfo = new MusicInfo(view, yScreen);
    }

    private void initBinder(IBinder service) {
        MusicUtils.toast(this, "initBinder");
        mMusicBinder = (MusicBinder) service;
        mMusicBinder.sendMsg(mMusicInfoReady, MusicCommand.REGISTER_INFO_READY);
        mMusicBar.setBinder(mMusicBinder);
        mMusicInfo.setBinder(mMusicBinder);
        mMusicSeekBar.setBinder(mMusicBinder);
    }

    private void serviceConnect() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                findViewById();
                initBinder(service);
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
