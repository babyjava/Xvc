package music;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.Media;

import java.util.Random;

import codelala.xvc.Command;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class MusicInfo {

    private static MusicInfo instance = new MusicInfo();
    private MusicInfoReadyListener mMusicInfoReadyListener;
    private final String[] mMusicItem = new String[]{Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DATA};
    private String[][] mMusicList;
    private int mMusicItemCount = mMusicItem.length;
    private int mPlayMode;
    private int mDataSourceArray;
    private int mMusicListLength;
    private int mPlayMusicIndex;
    private String mPlayingMusicInfo;
    private MusicInfo(){};

    public static MusicInfo getInstance(){
        return instance;
    }

    public interface MusicInfoReadyListener{
        void musicInfoReady(String[][] musicInfo);
        void musicPlayingInfo(String title, String artist, String ablum);
    }

    public void start(MusicInfoReadyListener listener, Context context) {
        mMusicInfoReadyListener = listener;
        new MusicInfoTask().execute(context);
    }

    private final String[][] getMusicInfo(Context context){
        String[][] musicList = null;
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, mMusicItem, null, null, null);
        if (cursor != null) {
            mMusicListLength = cursor.getCount();
            musicList = new String[mMusicItemCount][mMusicListLength];
            boolean notEnd = cursor.moveToFirst();
            int i = 0;
            while (notEnd) {
                for (int j = 0; j < mMusicItemCount; j++) {
                    musicList[j][i] = cursor.getString(cursor.getColumnIndex(mMusicItem[j]));
                }
                i++;
                notEnd = cursor.moveToNext();
            }
        }
        return musicList;
    }

    private final String[][] initPlayInfo(String[][] musicInfo, Context context) {
        if (musicInfo == null) return null;
        mPlayMode = context.getSharedPreferences(Command.MUSIC_PLAY_MODE, 0).getInt(Command.MUSIC_PLAY_MODE, Command.CLICK_MODE_LOOP);
        mPlayingMusicInfo = context.getSharedPreferences(Command.MUSIC_RECORD, 0).getString(Command.MUSIC_RECORD, null);
        mDataSourceArray = getDataSourceArray();
        if (mPlayingMusicInfo == null) {
            mPlayMusicIndex = 0;
            mPlayingMusicInfo = musicInfo[mDataSourceArray][mPlayMusicIndex];
        } else {
            boolean available = false;
            for (int i = 0; i < mMusicListLength; i++) {
                if (mPlayingMusicInfo.equals(musicInfo[mDataSourceArray][i])) {
                    mPlayMusicIndex = i;
                    available = true;
                    break;
                }
            }
            if (!available) {
                mPlayMusicIndex = 0;
                mPlayingMusicInfo = musicInfo[mDataSourceArray][mPlayMusicIndex];
            }
        }
        return musicInfo;
    }

    private class MusicInfoTask extends AsyncTask<Context, Void, String[][]> {

        @Override
        protected String[][] doInBackground(Context... params) {
            return initPlayInfo(getMusicInfo(params[0]), params[0]);
        }

        @Override
        protected void onPostExecute(String[][] strings) {
            super.onPostExecute(strings);
            mMusicList = strings;
            mMusicInfoReadyListener.musicInfoReady(strings);
        }
    }

    private final int getRandomIndex() {
        Random r = new Random();
        return r.nextInt(mMusicListLength);
    }

    private final int getLastIndex() {
        mPlayMusicIndex--;
        if (mPlayMusicIndex < 0) {
            mPlayMusicIndex = mMusicListLength - 1;
        }
        return mPlayMusicIndex;
    }

    private final int getNextIndex() {
        mPlayMusicIndex++;
        if (mPlayMusicIndex >= mMusicListLength) {
            mPlayMusicIndex = 0;
        }
        return mPlayMusicIndex;
    }

    private final int getDataSourceArray() {
        for (int j = 0; j < mMusicItemCount; j++) {
            if (mMusicItem[j].equals(Media.DATA)) {
                return j;
            }
        }
        return mMusicItemCount - 1;
    }

    private final String autoPlayDataSource() {
        switch (mPlayMode) {
            case Command.CLICK_MODE_LOOP:
                return mMusicList[mDataSourceArray][getNextIndex()];
            case Command.CLICK_MODE_RANDOM:
                return mMusicList[mDataSourceArray][getRandomIndex()];
            case Command.CLICK_MODE_SINGLE:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    public final String getDataSource(int arg) {
        switch (arg) {
            case Command.CLICK_LAST:
                return mMusicList[mDataSourceArray][getLastIndex()];
            case Command.CLICK_NEXT:
                return mMusicList[mDataSourceArray][getNextIndex()];
            case Command.CLICK_PLAY:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
            case Command.AUTO_PLAY:
                return autoPlayDataSource();
            default:
                return mMusicList[mDataSourceArray][mPlayMusicIndex];
        }
    }

    public final void setPlayMode(int mode, Context context) {
        mPlayMode = mode;
        context.getSharedPreferences(Command.MUSIC_PLAY_MODE, 0).edit().putInt(Command.MUSIC_PLAY_MODE, mode).commit();
    }
}
