package com.lecaoliem.songlist.Model;

import android.app.Application;

import com.lecaoliem.songlist.Service.MusicService;

public class GlobalClass extends Application {

    private MusicService musicService;

    public MusicService getMusicService() {
        return musicService;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }
}
