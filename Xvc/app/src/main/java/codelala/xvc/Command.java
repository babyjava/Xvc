package codelala.xvc;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public interface Command {
    String UTF8 = "utf-8";
    String MUSIC_RECORD = "music_record";
    String MUSIC_PLAY_MODE = "music_play_mode";
    int AUTO_PLAY = 1;
    int CLICK_PLAY = 0;
    int CLICK_NEXT = 2;
    int CLICK_LAST = 3;
    int CLICK_MODE_LOOP = 4;
    int CLICK_MODE_RANDOM = 5;
    int CLICK_MODE_SINGLE = 6;
}
