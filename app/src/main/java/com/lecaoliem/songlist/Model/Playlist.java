package com.lecaoliem.songlist.Model;

import java.io.Serializable;

public class Playlist implements Serializable {
    private int midPlayList;
    private String mPlaylistName;
    private String mSongNumber;

    public Playlist() {
    }

    public Playlist(int midPlayList, String mPlaylistName, String mSongNumber) {
        this.midPlayList = midPlayList;
        this.mPlaylistName = mPlaylistName;
        this.mSongNumber = mSongNumber;
    }

    public int getMidPlayList() {
        return midPlayList;
    }

    public void setMidPlayList(int midPlayList) {
        this.midPlayList = midPlayList;
    }

    public String getmPlaylistName() {
        return mPlaylistName;
    }

    public void setmPlaylistName(String mPlaylistName) {
        this.mPlaylistName = mPlaylistName;
    }

    public String getmSongNumber() {
        return mSongNumber;
    }

    public void setmSongNumber(String mSongNumber) {
        this.mSongNumber = mSongNumber;
    }
}
