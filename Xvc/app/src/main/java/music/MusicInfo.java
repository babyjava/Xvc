package music;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;

import codelala.xvc.MusicCommand;

/**
 * Created by Administrator on 2016/1/5 0005.
 * MusicInfo is just for data crud
 */
public final class MusicInfo {

    private final static String[] mMusicItem = new String[]{Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DATA};
    private final static int mMusicItemCount = mMusicItem.length;

    public final static String[][] crateMusicArray(Context context){
        String[][] musicArray = null;
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, mMusicItem, null, null, null);
        if (cursor != null) {
            int len = cursor.getCount();
            if (len < 1) {
                cursor.close();
                return null;
            }
            musicArray = new String[mMusicItemCount][len];
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
        cursor.close();
        return musicArray;
    }

    public final static int getDataSourceArray() {
        for (int j = 0; j < mMusicItemCount; j++) {
            if (mMusicItem[j].equals(Media.DATA)) {
                return j;
            }
        }
        return mMusicItemCount - 1;
    }

    public final static void savePlayingMusicInfo(Context context, String str) {
        context.getSharedPreferences(MusicCommand.MUSIC_RECORD, 0).edit().putString(MusicCommand.MUSIC_RECORD, str).commit();
    }

    public final static String getPlayingMusicInfo(Context context) {
        return context.getSharedPreferences(MusicCommand.MUSIC_RECORD, 0).getString(MusicCommand.MUSIC_RECORD, null);
    }

    public final static void savePlayMode(int mode, Context context) {
        context.getSharedPreferences(MusicCommand.MUSIC_PLAY_MODE, 0).edit().putInt(MusicCommand.MUSIC_PLAY_MODE, mode).commit();
    }

    public final static int getPlayMode(Context context) {
        return context.getSharedPreferences(MusicCommand.MUSIC_PLAY_MODE, 0).getInt(MusicCommand.MUSIC_PLAY_MODE, MusicCommand.CLICK_MODE_LOOP);
    }
}
