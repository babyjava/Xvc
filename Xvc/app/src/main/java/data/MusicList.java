package data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
/**
 * Created by Administrator on 2016/1/5 0005.
 */
public final class MusicList {

    public final static String[] mMusicItem = new String[]{Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DURATION, Media.DATA};

    public final static String[][] getMusicList(Context context){
        String[][] musicList = null;
        Cursor cursor = context.getContentResolver().query(Audio.Media.EXTERNAL_CONTENT_URI, mMusicItem, null, null, null);
        if (cursor != null) {
            int len = cursor.getCount();
            int size = mMusicItem.length;
            musicList = new String[size][len];
            boolean notEnd = cursor.moveToFirst();
            int i = 0;
            while (notEnd) {
                for (int j = 0; j < size; j++) {
                    musicList[j][i] = cursor.getString(cursor.getColumnIndex(mMusicItem[j]));
                }
                i++;
                notEnd = cursor.moveToNext();
            }
        }
        return musicList;
    }
}
