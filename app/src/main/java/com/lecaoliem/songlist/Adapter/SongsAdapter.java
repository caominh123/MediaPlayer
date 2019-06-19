package com.lecaoliem.songlist.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends BaseAdapter {
    private SQLiteDatabase database;
    private Context context;
    private int layout;
    private List<Song>songList;
    //private Database database;
    PlaylistAdapters playlistAdapters;
    ArrayList<Playlist> playlists;
    //public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;



    public SongsAdapter(Context context, int layout, List<Song> songList,SQLiteDatabase database) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
        this.database = database;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private  class ViewHolder{
        TextView txtSongTitle, txtArtist;
        ImageView imgHinh;
        ImageView imgMenuSong;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            // anh xa
            holder.txtSongTitle = (TextView) convertView.findViewById(R.id.textviewTen);
            holder.txtArtist = (TextView) convertView.findViewById(R.id.textviewArtist);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imageviewHinh);
            holder.imgMenuSong = (ImageView)convertView.findViewById(R.id.imageviewMenuSong) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //gan gia tri
        final Song song = songList.get(position);
        holder.txtSongTitle.setText(song.getmSongTitle());
        holder.txtArtist.setText(song.getmArtistName());
        if(song.getmImage() == 0)
            holder.imgHinh.setImageResource(R.drawable.music_picture);
        else
            holder.imgHinh.setImageResource(song.getmImage());

        holder.imgMenuSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, holder.txtSongTitle.getText(), Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context,holder.imgMenuSong);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
                //event item menu click
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuAdd:
                                DialogAddPlaylistOnSongs(song.getmIdSong());
                                break;
                            case R.id.menuDelete:
                                /*
                                database.delete("Song", "Id = ? ", new String[] {song.getmId() + ""} );
                                //database.QueryData("DELETE FROM Song WHERE Id='"+ song.getmId() +"'");
                                deleteSonginDevice(song.getmIdSong());
                                GetData();*/
                                delete(song.getmIdSong());
                                //Toast.makeText(context, "Deleted "+song.getmSongTitle(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menuInfo:
                                DialogSongInfo(song);
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });


        return convertView;
    }

    private void delete(final long idSongDevice) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_deletesong);
        dialog.setCanceledOnTouchOutside(false);

        final RadioGroup radioGroupDelete = dialog.findViewById(R.id.radioGroupDelete);
        Button btnOk =  dialog.findViewById(R.id.btnOKDelete);
        Button btnCancle = dialog.findViewById(R.id.btnCancleDelete);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idChecked = radioGroupDelete.getCheckedRadioButtonId();
                switch (idChecked)
                {
                    case R.id.radioDeleteDatabase:

                        database.delete("Song", "IdSong = ? ", new String[] {idSongDevice + ""} );
                        GetData();
                        Toast.makeText(context, "database", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioDeleteDevice:
                        database.delete("Song", "IdSong = ? ", new String[] {idSongDevice + ""} );
                        deleteSonginDevice(idSongDevice);
                        GetData();
                        Toast.makeText(context, "device", Toast.LENGTH_SHORT).show();
                        break;
                }
                database.delete("Song_Playlist","IdSong = ? ",new String[] {idSongDevice + ""});
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void deleteSonginDevice(long idSong) {
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri uri = ContentUris.withAppendedId(contentUri,idSong);
        int rows = context.getContentResolver().delete(uri, null, null);

        String path = uri.getEncodedPath();
        if(rows == 0)
        {
            Toast.makeText(context, "Example Code: Could not delete "+path+" "+idSong+"", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Example Code: delete "+path+" "+idSong+"", Toast.LENGTH_SHORT).show();
        }
    }


    public void DialogAddPlaylistOnSongs(final long idSong){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_playlist_in_song_playlist);
        dialog.setCanceledOnTouchOutside(false);
        //anh xa
        //TextView txtCreate = (TextView) dialog.findViewById(R.id.textviewCreate);
        final ListView lvPlaylist = (ListView) dialog.findViewById(R.id.listviewplaylist);
        //hiển thị danh sach playlist
        playlists = new ArrayList<Playlist>();
        GetDataPlaylist();
        playlistAdapters = new PlaylistAdapters(context,R.layout.playlist_item, playlists, database);
        lvPlaylist.setAdapter(playlistAdapters);

        lvPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idPlaylist = playlists.get(position).getMidPlayList();

                //thêm bài hát vài playlist
                ContentValues contentValues = new ContentValues();
                contentValues.put("IdSong",idSong);
                contentValues.put("IdPlaylist",idPlaylist);
                database.insert("Song_Playlist",null ,contentValues);//có lỗi
                //Cập nhật số lượng
                Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM Song_Playlist WHERE IdPlaylist ='"+idPlaylist+"'",null);
                cursor.moveToNext();
                int songNumber = cursor.getInt(0);
                ContentValues value = new ContentValues();
                value.put("SongNumber",songNumber);
                database.update("Playlist",value,"Id = ?", new String[]{idPlaylist + ""});
                Toast.makeText(context, idSong + " " + idPlaylist, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        //bắt sự kiện tạo playlist
        /*
        txtCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCreateNewPlaylist();
                dialog.dismiss();
                playlistAdapter.notifyDataSetChanged();
            }
        });*/

        dialog.show();

    }

    /*
    public void DialogCreateNewPlaylist(){
        final Dialog dialog = new Dialog(context);
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
                    Toast.makeText(context, "Please enter the playlist name!", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("INSERT INTO Playlist VALUES(null,'" + playlistName + "', null)");
                    Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }*/

    public void GetDataPlaylist(){
        //SELECT DATA
        Cursor dataPlaylist = database.rawQuery("SELECT * FROM Playlist",null);
        playlists.clear();
        while (dataPlaylist.moveToNext()){
            int id = dataPlaylist.getInt(0);
            String playlistName  = dataPlaylist.getString(1);
            String songNumber = dataPlaylist.getString(2);
            playlists.add(new Playlist(id, playlistName, songNumber));
        }
        //playlistAdapter.notifyDataSetChanged();
    }
    public void DialogSongInfo(Song song){
        AlertDialog.Builder dialogInfo = new AlertDialog.Builder(context);
        dialogInfo.setMessage("\t\t\t\t\t INFO SONG \n\nTitle: "+ song.getmSongTitle()+"\nArtist Name: "+ song.getmArtistName()+
                "\nLink: "+ song.getmLinkData()+"\nSize: "+ song.getmSize()+" Byte" + "\nID_song: " +song.getmIdSong());
        dialogInfo.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogInfo.show();

    }
    public void GetData(){
        Cursor dataSong = database.rawQuery("SELECT * FROM Song",null);
        songList.clear();
        while (dataSong.moveToNext()){
            int id = dataSong.getInt(0);
            String songTitle  = dataSong.getString(1);
            String songArtist = dataSong.getString(2);
            String songLink  = dataSong.getString(3);
            String songSize = dataSong.getString(4);
            long currentIdSong = dataSong.getLong(7);
            songList.add(new Song(id, songTitle, songArtist, songLink, songSize,currentIdSong));
        }
        this.notifyDataSetChanged();
    }




}
