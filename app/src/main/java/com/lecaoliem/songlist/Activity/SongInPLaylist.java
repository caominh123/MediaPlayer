package com.lecaoliem.songlist.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lecaoliem.songlist.Adapter.SongInPlaylistAdpter;
import com.lecaoliem.songlist.Adapter.SongsAdapter;
import com.lecaoliem.songlist.Model.GlobalClass;
import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.Model.Song_Playlist;
import com.lecaoliem.songlist.R;

import java.util.ArrayList;

public class SongInPLaylist extends AppCompatActivity {

    private final String DATABASE_NAME =  "mediaplayer.sqlite";
    private SQLiteDatabase database;

    SongInPlaylistAdpter songsAdapter;
    ArrayList<Song> arraySong;
    ArrayList<Song_Playlist> arraySongPlaylist;
    int idPlaylist;
    ListView lvSongInPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_in_playlist);

        database = com.lecaoliem.songlist.Activity.Database.initDatabase(this,DATABASE_NAME);//4592

        addControl();
        addEvent();

    }

    private void addEvent() {
        lvSongInPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GetDataSongPlaylist(arraySongPlaylist.get(position).getIdPlaylist());
                //String title = arraySong.get(position).getmSongTitle();
                //String artist = arraySong.get(position).getmArtistName();
                Intent intent = new Intent(SongInPLaylist.this, NowPlaying.class);
                //To pass:
                GlobalClass globalClass = (GlobalClass) getApplicationContext();
                globalClass.getMusicService().setList(arraySong);
                globalClass.getMusicService().setSong(position);
                //globalClass.getMusicService().setIdSong(arraySong.get(position).getmIdSong());//change
                startActivity(intent);

            }
        });
    }

    private void addControl() {
        Intent intent = this.getIntent();
        idPlaylist = intent.getIntExtra("idPlaylist",0);
        lvSongInPlaylist = findViewById(R.id.listviewSongInPlaylist);
        arraySong = new ArrayList<Song>();
        //arraySongPlaylist = new ArrayList<Song_Playlist>();
        //CreateDatabase();//arraysongplaylist
        GetDataSongPlaylist();
        //GetData();
        songsAdapter = new SongInPlaylistAdpter(this, R.layout.song_list_item, arraySong, database, idPlaylist);
        lvSongInPlaylist.setAdapter(songsAdapter);
        //songsAdapter.notifyDataSetChanged();
    }

    private void CreateDatabase() {

        //database = new Database(this,"media.sqlite", null,1);
        //GetDataSongPlaylist();
        arraySongPlaylist = new ArrayList<Song_Playlist>();
        Cursor cursor = database.rawQuery("SELECT * FROM Song_Playlist",null);
        arraySongPlaylist.clear();
        while (cursor.moveToNext()){
            int idSong = cursor.getInt(0);
            int idPlaylist = cursor.getInt(1);
            arraySongPlaylist.add(new Song_Playlist(idSong,idPlaylist));
        }


    }

    public void GetDataSongPlaylist(){
        //SELECT DATA
        /*Cursor data = database.GetData("SELECT Song.Id,Song.Title,Song.ArtistName,Song.LinkData,Song.Size" +
                                            " FROM Song_Playlist,Song" +
                                            " WHERE Song.Id = Song_Playlist.IdSong " +
                                            " GROUP BY IdPlaylist= '"+idPlaylist+"'");*/

        Cursor data = database.rawQuery("SELECT Song.Id,Song.Title,Song.ArtistName,Song.LinkData,Song.Size,Song.IdSong" +
                " FROM Song_Playlist,Song" +
                " WHERE Song.IdSong = Song_Playlist.IdSong " +
                " And IdPlaylist= '"+idPlaylist+"'",null);
        //Cursor data = database.rawQuery("Select * from Song",null);
        //arraySong.clear();
        while (data.moveToNext()){
            int id = data.getInt(0);
            String songTitle  = data.getString(1);
            String songArtist = data.getString(2);
            String songLink  = data.getString(3);
            String songSize = data.getString(4);
            long IdSong = data.getLong(5);
            arraySong.add(new Song(id, songTitle, songArtist, songLink, songSize,IdSong));

        }
        //playlistAdapter.notifyDataSetChanged();
    }
    public void GetData(){
        Cursor dataSong = database.rawQuery("SELECT * FROM Song",null);
        arraySong.clear();
        while (dataSong.moveToNext()){
            int id = dataSong.getInt(0);
            String songTitle  = dataSong.getString(1);
            String songArtist = dataSong.getString(2);
            String songLink  = dataSong.getString(3);
            String songSize = dataSong.getString(4);
            arraySong.add(new Song(id, songTitle, songArtist, songLink, songSize));
        }
        //this.notifyDataSetChanged();
    }
}
