package com.example.xy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import com.example.base.Constant;
import net.sourceforge.pinyin4j.PinyinHelper;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

public class DataService extends Service {
    public static String[] basedata,albumpytag,album,albumtag,
            artistpytag,artisttag,folder,foldertag,title=null,artist,data;
    public static String[][] albumdetail,folderdetail,artistdetail,albumgate,artistgate;
    private MediaPlayer mplay;
    private boolean play;
    private OnPreparedListener plistener;
    private OnCompletionListener clistener;
    private int playindex,len,playmode;
    private ArrayList<Integer> list=new ArrayList<Integer>(),
            playrecord=new ArrayList<Integer>();
    public final IBinder databinder=new DataBinder();
    private Intent t;
    private Handler h;
    private Runnable r;
    private SharedPreferences sp;
    @Override
    public void onCreate() {
        h = new Handler();
        t=new Intent();
        data();
        mplay= new MediaPlayer();
        sp=getSharedPreferences(Constant.SHARE, 0);
        playindex=sp.getInt(Constant.INDEX, 0);
        super.onCreate();
    }
    private void sendInfo() {
        Intent i = new Intent();
        int j = mplay.getDuration();
        i.putExtra("duration", j);
        String s=artist[playindex];
        i.putExtra("artist", s);
        s=title[playindex];
        i.putExtra("title", s);
        i.setAction(Constant.INFO);
        sendBroadcast(i);
    }
    private void sendTime(){
        t.setAction(Constant.TIME);
        r = new Runnable() {
            @Override
            public void run() {
                int j=mplay.getCurrentPosition();
                t.putExtra("time", j);
                sendBroadcast(t);
                h.postDelayed(this, 500);
            }
        };
        h.post(r);
    }
    protected void autoPlay(){
        switch (playmode) {
            case Constant.PLAY_RANDOM:
                randomIndex();
                break;
            case Constant.PLAY_ORDER:
                if(playindex==len-1){
                    return;
                }
                next();
                break;
            case Constant.PLAY_SINGLE:
                break;
            default:
                next();
                break;
        }
        play();
    }
    private void randomIndex(){
        if(data==null||data.length==0)return;
        if(list.size()==0){
            for(int i=0;i<len;i++){
                list.add(i);
            }
        }
        Random r = new Random();
        int i=r.nextInt(list.size());
        playindex=list.get(i);
        list.remove(i);
    }
    private void next(){
        if(data==null||data.length==0)return;
        playindex++;
        playindex%=len;
    }
    private void play(){
        if(data==null||data.length==0)return;
        if(r!=null){
            h.removeCallbacks(r);}
        play=true;
        mplay.reset();
        try {
            mplay.setDataSource(data[playindex]);
            mplay.prepareAsync();
            if(plistener==null){
                plistener=new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mplay.start();
                        sendInfo();
                        sendTime();
                    }
                };
                mplay.setOnPreparedListener(plistener);
            }
            if(clistener==null){
                clistener=new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playrecord.add(playindex);
                        autoPlay();
                    }
                };
                mplay.setOnCompletionListener(clistener);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public DataService() {
    }
    public class DataBinder extends Binder{
        public void clickPlay(int i){
            playindex=i;
            DataService.this.play();
        }
        public void play() {
            DataService.this.play();
        }
        public void next(){
            if(playmode==Constant.PLAY_RANDOM){
                DataService.this.randomIndex();
            }else{
                DataService.this.next();
            }
            DataService.this.play();
        }
        public void last(){
            int i=playrecord.size();
            if(i>0){
                playindex=playrecord.get(i-1);
                playrecord.remove(i-1);
            }else{
                if(playindex<1){
                    playindex=len;
                }
                playindex--;
            }
            DataService.this.play();
        }
        public String title(){
            return (title==null||title.length==0)?"":title[playindex];
        }
        public String artist(){
            return (artist==null||artist.length==0)?"":artist[playindex];
        }
        public boolean isPlay(){
            return mplay.isPlaying();
        }
        public void onPlay(){
            mplay.start();
        }
        public void pause() {
            mplay.pause();
        }
        public void seekTo(int i){
            mplay.seekTo(i);
        }
        public void setMode(int i){
            playmode=i;
        }
        public int getDur(){
            if(play){
                return mplay.getDuration();
            }
            return -1;
        }
        public int getPosition(){
            return mplay.getCurrentPosition();
        }
        public int songIndex(){
            return playindex;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return databinder;
    }
    private void data(){
        HashSet<String> albumset=null,artistset=null,folderset=null;
        ContentResolver r = getContentResolver();
        Cursor c = r.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{Media.TITLE,Media.ALBUM,Media.ARTIST,Media.DURATION,Media.DATA},
                null, null, null);
//		 Media.DATE_MODIFIED;
        if(c!=null){
            albumset=new HashSet<String>();
            artistset=new HashSet<String>();
            folderset=new HashSet<String>();
            int i=c.getCount();
            basedata=new String[8];
            title=new String[i];album=new String[i];
            artist=new String[i];folder=new String[i];data=new String[i];
            boolean b=c.moveToFirst();
            i=0;
            while(b){
                try {
                    title[i] = new String(c.getString(c.getColumnIndex(Media.TITLE)).getBytes("utf-8"));
                    album[i]=new String(c.getString(c.getColumnIndex(Media.ALBUM)).getBytes("utf-8"));
                    artist[i]=new String(c.getString(c.getColumnIndex(Media.ARTIST)).getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                data[i]=c.getString(c.getColumnIndex(Media.DATA)).toString();
                folder[i]=data[i].substring(0, data[i].lastIndexOf("/"));
                albumset.add(album[i]);
                artistset.add(artist[i]);
                folderset.add(folder[i]);
                i++;
                b=c.moveToNext();
            }
            c.close();
            len=title.length;
            int a=albumset.size();
            basedata[4]=a+"";
            albumdetail=new String[a][];
            albumtag=(String[]) albumset.toArray(new String[a]);
            a=folderset.size();
            foldertag =(String[]) folderset.toArray(new String[a]);
            folderdetail=new String[a][];
            basedata[2]=a+"";
            a=artistset.size();
            artisttag=(String[]) artistset.toArray(new String[a]);
            artistdetail=new String[a][];
            basedata[3]=a+"";
            basedata[0]="";basedata[1]=i+"";
            basedata[5]="0";basedata[6]="";basedata[7]="0";
            pinYinGate();
            detailGate();
        }
    }
    private void pinYinGate(){
        albumpytag=pinYinTag(albumtag);
        artistpytag=pinYinTag(artisttag);
        albumgate=pinYinList(albumpytag,albumtag);
        artistgate=pinYinList(artistpytag,artisttag);
    }
    private void detailGate(){
        albumdetail=listindex(albumtag,album);
        folderdetail=listindex(foldertag,folder);
        artistdetail=listindex(artisttag,artist);
    }
    private  String[] pinYinTag(String[] s){
        HashSet<String> set = new HashSet<String>();
        String[] tag=null;char c;
        for(int i=0, j=s.length;i<j;i++){
            c=s[i].charAt(0);
            if(c>128){
                tag=PinyinHelper.toHanyuPinyinStringArray(c);
                c=(tag==null?'z':tag[0].charAt(0));
            }
            set.add(String.valueOf(c));
        }
        tag=(String[]) set.toArray(new String[set.size()]);
        Arrays.sort(tag);
        return tag;
    }
    private  String[][] pinYinList(String[] t,String[] s){
        String a; int z=0,j=t.length,y=s.length;
        String[] list = new String[j+y],hanzi; char c;
        String[][] str=new String[j+1][];
        ArrayList<String> al=null;
        for(int i=0;i<j;i++){
            al=new ArrayList<String>();
            a=t[i];
            list[z]=a;
            z++;
            for(int x=0;x<y;x++){
                c=s[x].charAt(0);
                if(c>128){
                    hanzi=PinyinHelper.toHanyuPinyinStringArray(c);
                    c=(hanzi==null?'z':hanzi[0].charAt(0));
                }
                if(a.equals(String.valueOf(c))){
                    al.add(s[x]);
                    list[z]=s[x];
                    z++;
                }
            }
            str[i]=al.toArray(new String[al.size()]);
        }
        str[j]=list;
        return str;
    }
    private  String[][] listindex(String[] s,String[] t){
        String tag=null,list=null;
        ArrayList<String> index=null;
        int j=s.length;
        String[][] listindex=new String[j][];
        for(int i=0;i<j;i++){
            tag=s[i];
            index = new ArrayList<String>();
            for(int a=0,b=t.length;a<b;a++){
                list=t[a];
                if(tag.equals(list)){
                    index.add(String.valueOf(a));
                }
            }
            listindex[i]=index.toArray(new String[index.size()]);
        }
        return listindex;
    }
}
