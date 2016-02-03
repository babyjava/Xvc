package test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import codelala.xvc.R;

public class Test extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private String[][] mMusicList;
    private MyAdapter mMyAdapter;

    public void setArguments(String[][] musicList) {
        mMusicList = musicList;
        if (mMyAdapter != null) {
            mMyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.view_music_pull_layout, container, false);
            mRecyclerView = (RecyclerView) mView.findViewById(R.id.view_pager);
            mMyAdapter = new MyAdapter();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mMyAdapter);
        } else {
            if (mView.getParent() != null) {
                container = (ViewGroup) mView.getParent();
                container.removeView(mView);
            }
        }
        return mView;
    }

    private final class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView title, artist, album;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.musiclist_title);
                artist = (TextView) itemView.findViewById(R.id.musiclist_artist);
                album = (TextView) itemView.findViewById(R.id.musiclist_album);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_musiclist, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.title.setText("123");
            holder.artist.setText("123");
            holder.album.setText("123" + position);
        }

        @Override
        public int getItemCount() {
            return mMusicList == null?0:mMusicList.length;
        }
    }

}
