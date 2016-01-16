package codelala.xvc;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public interface Command {
    String UTF8 = "utf-8";
    String MUSIC_RECORD = "music_record";
    String MUSIC_PLAY_MODE = "music_play_mode";
    int SYSTEM_ERROR = -99;
    int CLICK_PLAY_PLAY = 0;
    int CLICK_PLAY_NEXT = 1;
    int CLICK_PLAY_LAST = 2;
    int CLICK_MODE_LOOP = 3;
    int CLICK_MODE_RANDOM = 4;
    int CLICK_MODE_SINGLE = 5;
    int TOUCH_SEEKBAR_SET_MUSIC_PLAY_POSTION = 6;
    int REGISTER_PLAYING_STATUS = 7;
    int REGISTER_PLAYING_INFO = 8;
    int REGISTER_SEEKBAR_STATUS = 9;
    int REGISTER_INFO_READY = 10;
}
