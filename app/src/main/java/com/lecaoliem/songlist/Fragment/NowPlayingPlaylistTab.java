package com.lecaoliem.songlist.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lecaoliem.songlist.Activity.NowPlaying;
import com.lecaoliem.songlist.Adapter.SongsAdapter;
import com.lecaoliem.songlist.Model.GlobalClass;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;
import com.lecaoliem.songlist.Service.MusicService;

import java.util.ArrayList;



public class NowPlayingPlaylistTab extends ListFragment {
    ArrayList<Song> arraysong ;
    SongsAdapter songsAdapter;
    private final String DATABASE_NAME =  "mediaplayer.sqlite";
    private SQLiteDatabase database;
    public MusicService musicService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.now_playing_playlist_tab, container,false);
        View view = (ViewGroup) inflater.inflate(R.layout.tab_list_song, container,false);
        database = com.lecaoliem.songlist.Activity.Database.initDatabase(getActivity(),DATABASE_NAME);
        //To retrieve object in second Activity
        //String idSong = getActivity().getIntent().getStringExtra("idsong");
        //arraysong = (ArrayList<Song>) getActivity().getIntent().getSerializableExtra("array");
        GlobalClass globalClass = (GlobalClass) getActivity().getApplicationContext();
        musicService = globalClass.getMusicService();
        arraysong = musicService.getArraySong();
        songsAdapter = new SongsAdapter(getActivity(),R.layout.song_list_item, arraysong, database);
        setListAdapter(songsAdapter);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Intent intent = new Intent(getActivity(), NowPlaying.class);
        GlobalClass globalClass = (GlobalClass) getActivity().getApplicationContext();
        //globalClass.getMusicService().setList(arraysong);
        globalClass.getMusicService().setSong(position);
        startActivity(getActivity().getIntent());
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        //getActivity().recreate();
        //NowPlaying nowPlaying = new NowPlaying();

    }
}
