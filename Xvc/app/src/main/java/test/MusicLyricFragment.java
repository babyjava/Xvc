package test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import codelala.xvc.R;

public class MusicLyricFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_music_list, container, false);
        } else {
            if (mView.getParent() != null) {
                container = (ViewGroup) mView.getParent();
                container.removeView(mView);
            }
        }
        return mView;
    }


}
