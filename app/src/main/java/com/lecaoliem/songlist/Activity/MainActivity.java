package com.lecaoliem.songlist.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.lecaoliem.songlist.Adapter.MainViewPagerAdapter;
import com.lecaoliem.songlist.Adapter.PlaylistAdapter;
import com.lecaoliem.songlist.Adapter.SongsAdapter;
import com.lecaoliem.songlist.Fragment.Fragment_Album;
import com.lecaoliem.songlist.Fragment.Fragment_Artist;
import com.lecaoliem.songlist.Fragment.Fragment_Playlist;
import com.lecaoliem.songlist.Fragment.Fragment_Song_List;
import com.lecaoliem.songlist.Model.GlobalClass;
import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;
import com.lecaoliem.songlist.Service.MusicService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    GlobalClass globalClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        initMainView();
        globalClass = (GlobalClass)getApplicationContext();
        tabLayout.setSelectedTabIndicator(R.drawable.ic_music_red);

    }
    @Override
    protected void onStart() {
        super.onStart();

        // Start Music Service
        if (playIntent == null) {
            // Ràng buộc service với Activity
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);

        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    public ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            //get service
            musicService = binder.getService();
            //Truyền List nhạc vào Service
            //musicService.setList(songList);
            musicBound = true;
            // Gán Global Variable cho MusicService
            globalClass.setMusicService(musicService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    private void initMainView() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new Fragment_Song_List(),"Songs");
        mainViewPagerAdapter.addFragment(new Fragment_Playlist(),"PlayList");

        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_music);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_playlist);




    }
    private void anhxa() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);

    }

}
