package view;

import codelala.xvc.MusicBinder;
import codelala.xvc.MusicUtils;

/**
 * Created by Administrator on 2016/2/4 0004.
 */
public abstract class BaseView {
    abstract void setBinder(MusicBinder musicBinder);
    abstract int setYLocation(int y);
    public void log(String s) {
        MusicUtils.log(s);
    }
}
