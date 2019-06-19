package com.lecaoliem.songlist.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Activity.NowPlaying;
import com.lecaoliem.songlist.Adapter.SongsAdapter;
import com.lecaoliem.songlist.Model.GlobalClass;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fragment_Song_List extends ListFragment{
    private final String DATABASE_NAME =  "mediaplayer.sqlite";
    private SQLiteDatabase database;

    View view;
    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arraySearch;
    ArrayList<Song> arraySong ;
    ArrayList<Song> arraySongDatabase = new ArrayList<Song>();
    SongsAdapter songsAdapter,songsAdapterSearch;
    ArrayAdapter<String> adapterSearch;
    Button btnShuffle;
    ImageButton imgBack;
    AutoCompleteTextView autoSearch;
    ImageButton imgSearch;
    //Database database;
    ProgressDialog p;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_list,container,false);
        database = com.lecaoliem.songlist.Activity.Database.initDatabase(getActivity(),DATABASE_NAME);
        AnhXa();
        CheckRole();
        initDatabase();
        songsAdapter = new SongsAdapter(getActivity(),R.layout.song_list_item, arraySongDatabase,database);
        setListAdapter(songsAdapter);
        //update database
        //MyTask myTask = new MyTask();
        //myTask.execute();

        //bắt sự kiện shuffle
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Shuffle",Toast.LENGTH_SHORT).show();
            }
        });
        getSong();
        autoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterSearch = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,arraySearch);
                autoSearch.setAdapter(adapterSearch);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSong();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSearch.setText("");
                setListAdapter(songsAdapter);
            }
        });

        return view;
    }

    private void searchSong()
    {
        String title = autoSearch.getText().toString();
        arraySong = new ArrayList<>();

        Cursor dataSong = database.rawQuery("SELECT * FROM Song WHERE Title = '"+title+"'",null);
        while (dataSong.moveToNext()){
            int id = dataSong.getInt(0);
            String songTitle  = dataSong.getString(1);
            String songArtist = dataSong.getString(2);
            String songLink  = dataSong.getString(3);
            String songSize = dataSong.getString(4);
            long idSong = dataSong.getLong(7);
            arraySong.add(new Song(id, songTitle, songArtist, songLink, songSize,idSong));
        }
        songsAdapterSearch = new SongsAdapter(getActivity(),R.layout.song_list_item, arraySong,database);
        setListAdapter(songsAdapterSearch);

    }

    private void getSong()
    {
        arraySearch = new ArrayList<>();
     Cursor cursor = database.rawQuery("SELECT Song.Title FROM Song",null);
     while (cursor.moveToNext())
     {
         String title = cursor.getString(0);
         arraySearch.add(title);
     }

    }
    public void CheckRole(){
        // check quyền truy cập phương tiện media
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);

            } else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }

        }else {
            doStuff();
        }
    }
    /*
    public class MyTask extends AsyncTask<Integer, String, ArrayList<Song>> {

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getActivity());
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected ArrayList<Song> doInBackground(Integer... integers) {

            return arraySongDatabase;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Song> s) {
            super.onPostExecute(s);
            if(s!=null) {
                p.hide();

            }else {
                p.show();
            }

            //songsAdapter.notifyDataSetChanged();

        }
    }*/

    private void initDatabase() {
        //tạo database
        //database = new Database(getActivity(),"media.sqlite", null,1);
        //tạo bảng Songs
        //database.QueryData("CREATE TABLE IF NOT EXISTS Song(Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                //" Title TEXT, ArtistName TEXT, LinkData TEXT, Size TEXT, PlaylistName TEXT, AlbumName TEXT,IdSong TEXT)");
        //Delete data
        database.delete("Song",null,null);
        //insert data

        if(arraySong != null) {
            for (Song song : arraySong) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("Title",song.getmSongTitle());
                contentValues.put("ArtistName",song.getmArtistName());
                contentValues.put("LinkData",song.getmLinkData());
                contentValues.put("Size",song.getmSize());
                contentValues.put("IdSong",song.getmIdSong());

                database.insert("Song",null ,contentValues);

                /*database.QueryData("INSERT INTO Song VALUES(null,'" + song.getmSongTitle() + "','" + song.getmArtistName() +
                        "','" + song.getmLinkData() + "','" + song.getmSize() + "', null, null,'"+song.getmIdSong()+"')");*/
            }
        }
        getArraySongDatabase();

    }
    public void getArraySongDatabase(){
        //SELECT DATA
        Cursor dataSong = database.rawQuery("SELECT * FROM Song",null);
        arraySongDatabase.clear();
        while (dataSong.moveToNext()){
            int id = dataSong.getInt(0);
            String songTitle  = dataSong.getString(1);
            String songArtist = dataSong.getString(2);
            String songLink  = dataSong.getString(3);
            String songSize = dataSong.getString(4);
            int idSong = dataSong.getInt(7);
            arraySongDatabase.add(new Song(id, songTitle, songArtist, songLink, songSize,idSong));
        }
    }
    //Ánh xạ biến
    private void AnhXa() {
        btnShuffle = view.findViewById(R.id.buttonShuffle);
        imgBack =  view.findViewById(R.id.buttonBack);
        imgSearch =  view.findViewById(R.id.buttonSearch);
        autoSearch =  view.findViewById(R.id.autoSearch);
    }

    // hiển thị ds nhạc lên  ListFragment
    public void doStuff(){
        arraySong = new ArrayList<Song>();
        getMusic();


    }

    //Lấy song từ local lên arraylist
    public void getMusic(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);

        if(songCursor != null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLinkData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songSize = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int songID = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLink = songCursor.getString(songLinkData);
                String currentSize = songCursor.getString(songSize);
                long currentIdSong = songCursor.getLong(songID);
                arraySong.add(new Song(currentTitle,currentArtist,currentLink,currentSize,currentIdSong));
            }while (songCursor.moveToNext());
        }
    }

    // Bắt request từ local và trả bài hát
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ){
                        Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();
                        doStuff();
                    } else {
                        Toast.makeText(getActivity(), "No permission granted!", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                    return;
                }
            }
        }
    }

    // sự kiện click vào bài nhạc
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), arraySongDatabase.get(position).getmSongTitle(), Toast.LENGTH_SHORT).show();
        getArraySongDatabase();
        Intent intent = new Intent(getActivity(), NowPlaying.class);
        GlobalClass globalClass = (GlobalClass) getActivity().getApplicationContext();

        globalClass.getMusicService().setList(arraySongDatabase);
        globalClass.getMusicService().setSong(position);
        //globalClass.getMusicService().setIdSong(arraySongDatabase.get(position).getmIdSong());//change
        getActivity().startActivity(intent);
        //startActivity(intent);
    }

}
