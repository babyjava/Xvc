package codelala.xvc;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class CallBack {

    private CallBack(){};

    public interface MusicPlayingStatus {
        void musicPlayingStatus(boolean isPlaying);
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
