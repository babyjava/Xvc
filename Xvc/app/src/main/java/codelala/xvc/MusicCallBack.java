package codelala.xvc;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class MusicCallBack {

    private MusicCallBack(){};

    public interface MusicPlayBarListener {
        void musicPlayBarStatus(boolean isPlaying);
    }

    public interface MusicPlayingInfo {
        void musicPlayingInfo(String artist, String title, String ablum, int time);
    }

    public interface MusicSeekBarStatus {
        void musicSeekBarStatus(boolean isPlaying, int position, int time);
    }
    public interface MusicInfoReady {
        void musicInfoReady(String[][] musicInfo);
    }
}
