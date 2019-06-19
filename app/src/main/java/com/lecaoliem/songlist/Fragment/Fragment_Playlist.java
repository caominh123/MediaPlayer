package com.lecaoliem.songlist.Fragment;

import android.app.Dialog;
import android.arch.persistence.room.Database;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Activity.SongInPLaylist;
import com.lecaoliem.songlist.Adapter.PlaylistAdapter;
import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.R;

import java.util.ArrayList;

public class Fragment_Playlist extends ListFragment {
    private final String DATABASE_NAME =  "mediaplayer.sqlite";
    private SQLiteDatabase database;

    View view;
    ArrayList<Playlist> playlists;
    PlaylistAdapter playlistAdapter;
    Button btnCreatePlaylist;
    RecyclerView lstPlaylist;
    //Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist,container,false);

        database = com.lecaoliem.songlist.Activity.Database.initDatabase(getActivity(),DATABASE_NAME);
        playlists = new ArrayList<Playlist>();
        //tạo database
        CreateDatabase();
        CatchEventCreatePlaylist();
        playlistAdapter = new PlaylistAdapter(getActivity(), playlists ,database);
        //set(new GridLayoutManager(getActivity(),2));
        setListAdapter((ListAdapter) playlistAdapter);
        //setListAdapter(playlistAdapter);
        //Bắt sự kiện create playlist



        return view;
    }

    private void CatchEventCreatePlaylist() {
        //Ánh xạ và bắt sự kiên create playlist
        //lstPlaylist = view.findViewById(android.R.id.list);
        btnCreatePlaylist = view.findViewById(R.id.buttonCreatePlaylist);
        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCreateNewPlaylist();
            }
        });
    }

    public void DialogCreateNewPlaylist(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtNamePlaylist = (EditText) dialog.findViewById(R.id.edittextPlaylistName);
        Button btnCancle = (Button) dialog.findViewById(R.id.buttonCancle);
        Button btnOk = (Button) dialog.findViewById(R.id.buttonOK);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlistName = edtNamePlaylist.getText().toString();
                if(playlistName.equals("")){
                    Toast.makeText(getActivity(), "Please enter the playlist name!", Toast.LENGTH_SHORT).show();
                }else {
                    int numberSong = 0;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("PlaylistName",playlistName);
                    contentValues.put("SongNumber",numberSong);

                    database.insert("Playlist",null ,contentValues);
                    //database.QueryData("INSERT INTO Playlist VALUES(null,'" + playlistName + "', null)");
                    Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataPlaylist();
                }
            }
        });
        dialog.show();
    }

    private void CreateDatabase() {

        //database = new Database(getActivity(),"media.sqlite", null,1);
        //tạo bảng Playlist
        /*database.QueryData("CREATE TABLE IF NOT EXISTS Playlist(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PlaylistName TEXT, SongNumber INT)");
        database.QueryData("CREATE TABLE IF NOT EXISTS Song_Playlist" +
                "(IdSong INTEGER NOT NULL ,IdPlaylist INTEGER NOT NULL,PRIMARY KEY(IdSong,IdPlaylist))");*/
        GetDataPlaylist();

    }
    // đang làm tới đây
    public void GetDataPlaylist(){
        //SELECT DATA
        //Cursor dataPlaylist = database.GetData("SELECT * FROM Playlist");
        Cursor dataPlaylist = database.rawQuery("SELECT * FROM Playlist",null);
        playlists.clear();
        while (dataPlaylist.moveToNext()){
            int id = dataPlaylist.getInt(0);
            String playlistName  = dataPlaylist.getString(1);
            String songNumber = dataPlaylist.getString(2);
            playlists.add(new Playlist(id, playlistName, songNumber));
        }
        playlists.add(new Playlist(10,"s","2"));
        playlists.add(new Playlist(15,"a","1"));
        //playlistAdapter.notifyDataSetChanged();
    }
    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        playlistAdapter.notifyDataSetChanged();
        int idPlaylist  = playlists.get(position).getMidPlayList();
        Toast.makeText(getActivity(), idPlaylist + "", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), SongInPLaylist.class);
        //To pass:
        intent.putExtra("idPlaylist",idPlaylist);
        getActivity().startActivity(intent);
    }*/
}
