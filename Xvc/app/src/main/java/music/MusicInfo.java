package music;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import java.util.Random;
import codelala.xvc.Command;

/**
 * Created by Administrator on 2016/1/5 0005.
 * MusicInfo is just for data crud
 */
public final class MusicInfo {

    private final static String[] mMusicItem = new String[]{Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DATA};
    private final static int mMusicItemCount = mMusicItem.length;

    private static int mPlayMode;
    private static int mDataSourceArray;
    private static int mMusicListLength;
    private static int mPlayMusicIndex;
    private static String mPlayingMusicInfo;

    public final static String[][] crateMusicArray(Context context){
        String[][] musicArray = null;
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, mMusicItem, null, null, null);
        if (cursor != null) {
            mMusicListLength = cursor.getCount();
            musicArray = new String[mMusicItemCount][mMusicListLength];
            boolean notEnd = cursor.moveToFirst();
            int i = 0;
            while (notEnd) {
                for (int j = 0; j < mMusicItemCount; j++) {
                    musicArray[j][i] = cursor.getString(cursor.getColumnIndex(mMusicItem[j]));
                }
                i++;
                notEnd = cursor.moveToNext();
            }
        }
        return musicArray;
    }

    private final static int getRandomIndex() {
        Random r = new Random();
        return r.nextInt(mMusicListLength);
    }

    private final static int getLastIndex() {
        mPlayMusicIndex--;
        if (mPlayMusicIndex < 0) {
            mPlayMusicIndex = mMusicListLength - 1;
        }
        return mPlayMusicIndex;
    }

    private final static int getNextIndex() {
        mPlayMusicIndex++;
        if (mPlayMusicIndex >= mMusicListLength) {
            mPlayMusicIndex = 0;
        }
        return mPlayMusicIndex;
    }

    public final static int getDataSourceArray() {
        for (int j = 0; j < mMusicItemCount; j++) {
            if (mMusicItem[j].equals(Media.DATA)) {
                return j;
            }
        }
        return mMusicItemCount - 1;
    }

//    public final static String autoPlayDataSource() {
//        switch (mPlayMode) {
//            case Command.CLICK_MODE_LOOP:
//                return mMusicList[mDataSourceArray][getNextIndex()];
//            case Command.CLICK_MODE_RANDOM:
//                return mMusicList[mDataSourceArray][getRandomIndex()];
//            case Command.CLICK_MODE_SINGLE:
//                return mMusicList[mDataSourceArray][mPlayMusicIndex];
//            default:
//                return mMusicList[mDataSourceArray][mPlayMusicIndex];
//        }
//    }
//
//    public final static String getDataSource(int arg) {
//        switch (arg) {
//            case Command.CLICK_LAST:
//                return mMusicList[mDataSourceArray][getLastIndex()];
//            case Command.CLICK_NEXT:
//                return mMusicList[mDataSourceArray][getNextIndex()];
//            case Command.CLICK_PLAY:
//                return mMusicList[mDataSourceArray][mPlayMusicIndex];
//            case Command.AUTO_PLAY:
//                return autoPlayDataSource();
//            default:
//                return mMusicList[mDataSourceArray][mPlayMusicIndex];
//        }
//    }

    public final static String getPlayingMusicInfo(Context context) {
        return context.getSharedPreferences(Command.MUSIC_RECORD, 0).getString(Command.MUSIC_RECORD, null);
    }

    public final static void setPlayMode(int mode, Context context) {
        context.getSharedPreferences(Command.MUSIC_PLAY_MODE, 0).edit().putInt(Command.MUSIC_PLAY_MODE, mode).commit();
    }

    public final static int getPlayMode(Context context) {
        return context.getSharedPreferences(Command.MUSIC_PLAY_MODE, 0).getInt(Command.MUSIC_PLAY_MODE, Command.CLICK_MODE_LOOP);
    }
}
