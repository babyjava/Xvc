package music;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Audio.Media;
/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class MusicInfo {

    private static MusicInfo instance = new MusicInfo();
    private MusicInfoReadyListener mMusicInfoReadyListener;
    private final String[] mMusicItem = new String[]{Media.TITLE, Media.ALBUM, Media.ARTIST, Media.DATA};
    private int mMusicInfoSize = mMusicItem.length;

    private MusicInfo(){};

    public static MusicInfo getInstance(){
        return instance;
    }

    public interface MusicInfoReadyListener{
        void musicInfoReady(String[][] musicInfo);
    }

    public void start(MusicInfoReadyListener listener, Context context) {
        mMusicInfoReadyListener = listener;
        new MusicInfoTask().execute(context);
    }

    private final String[][] getMusicInfo(Context context){
        String[][] musicList = null;
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, mMusicItem, null, null, null);
        if (cursor != null) {
            int len = cursor.getCount();
            musicList = new String[mMusicInfoSize][len];
            boolean notEnd = cursor.moveToFirst();
            int i = 0;
            while (notEnd) {
                for (int j = 0; j < mMusicInfoSize; j++) {
                    musicList[j][i] = cursor.getString(cursor.getColumnIndex(mMusicItem[j]));
                }
                i++;
                notEnd = cursor.moveToNext();
            }
        }
        return musicList;
    }

    private class MusicInfoTask extends AsyncTask<Context, Void, String[][]> {

        @Override
        protected String[][] doInBackground(Context... params) {
            return getMusicInfo(params[0]);
        }

        @Override
        protected void onPostExecute(String[][] strings) {
            super.onPostExecute(strings);
            mMusicInfoReadyListener.musicInfoReady(strings);
        }
    }

    public int getDataSourceIndex() {
        for (int j = 0; j < mMusicInfoSize; j++) {
            if (mMusicItem[j].equals(Media.DATA)) {
                return j;
            }
        }
        return mMusicInfoSize - 1;
    }


}
