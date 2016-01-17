package codelala.xvc;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Test extends FragmentActivity {

    private String[][] mMusicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        RecyclerView t = (RecyclerView) findViewById(R.id.test_recyclerview);
        t.setLayoutManager(new LinearLayoutManager(this));
        t.setAdapter(new MyAdapter());
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
            LayoutInflater l = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return new MyViewHolder(l.inflate(R.layout.item_musiclist, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.title.setText("123");
            holder.artist.setText("123");
            holder.album.setText("123");
        }

        @Override
        public int getItemCount() {
            return mMusicList == null?100:mMusicList.length;
        }
    }


}
