package com.lecaoliem.songlist.Model;

public class Song_Playlist {
    private int idSong;
    private int idPlaylist;

    public Song_Playlist(int idSong, int idPlaylist) {
        this.idSong = idSong;
        this.idPlaylist = idPlaylist;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
}
