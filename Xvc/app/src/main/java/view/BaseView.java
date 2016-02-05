package view;

import codelala.xvc.MusicPlayService.MusicBinder;
import codelala.xvc.MusicUtils;

/**
 * Created by Administrator on 2016/2/4 0004.
 */
public abstract class BaseView {
    public abstract void setBinder(MusicBinder musicBinder);
    public abstract int setLocation(int y);
    public void log(String s) {
        MusicUtils.log(s);
    }
}
