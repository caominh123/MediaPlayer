package com.lecaoliem.songlist.Adapter;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;

import java.util.ArrayList;
import java.util.List;

public class SongInPlaylistAdpter extends BaseAdapter {
    private SQLiteDatabase database;
    private Context context;
    private int layout;
    private List<Song>songList;
    //private Database database;
    PlaylistAdapters playlistAdapters;
    ArrayList<Playlist> playlists;
    private int IdPlaylist;



    public SongInPlaylistAdpter(Context context, int layout, List<Song> songList,SQLiteDatabase database,int IdPPlaylist) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
        this.database = database;
        this.IdPlaylist = IdPPlaylist;
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
        final SongInPlaylistAdpter.ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new SongInPlaylistAdpter.ViewHolder();
            // anh xa
            holder.txtSongTitle = (TextView) convertView.findViewById(R.id.textviewTen);
            holder.txtArtist = (TextView) convertView.findViewById(R.id.textviewArtist);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imageviewHinh);
            holder.imgMenuSong = (ImageView)convertView.findViewById(R.id.imageviewMenuSong) ;
            convertView.setTag(holder);
        }
        else{
            holder = (SongInPlaylistAdpter.ViewHolder) convertView.getTag();
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
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song_in_playlist,popupMenu.getMenu());
                //event item menu click
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuAddToPlaylist:
                                DialogAddPlaylistOnSongs(song.getmIdSong());
                                break;
                            case R.id.menuDeleteInPlaylist:
                                database.delete("Song_Playlist","IdSong = ? AND IdPlaylist = ?" , new String[] {song.getmIdSong() +"" ,IdPlaylist + ""});
                                //database.rawQuery("DELETE FROM Song_Playlist WHERE Idsong= '"+song.getmId()+"'",null);
                                GetDataSongPlaylist();
                                break;
                                /*
                                database.delete("Song", "Id = ? ", new String[] {song.getmId() + ""} );
                                //database.QueryData("DELETE FROM Song WHERE Id='"+ song.getmId() +"'");
                                deleteSonginDevice(song.getmIdSong());
                                GetData();
                                //Toast.makeText(context, "Deleted "+song.getmSongTitle(), Toast.LENGTH_SHORT).show();
                                break;*/
                            case R.id.menuInfoInPlaylist:
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
                ContentValues contentValues = new ContentValues();
                contentValues.put("IdSong",idSong);
                contentValues.put("IdPlaylist",idPlaylist);
                //có lỗi
                //Cursor cursor = database.GetData("SELECT * FROM Song_Playlist WHERE IdSong ='"+idSong+"' AND IdPlaylist= '"+idPlaylist+"'");
                database.insert("Song_Playlist",null ,contentValues);
                //database.QueryData("INSERT INTO Song_Playlist VALUES('" + idSong + "','" + idPlaylist + "')");
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
    public void GetDataSongPlaylist(){
        //SELECT DATA
        /*Cursor data = database.GetData("SELECT Song.Id,Song.Title,Song.ArtistName,Song.LinkData,Song.Size" +
                                            " FROM Song_Playlist,Song" +
                                            " WHERE Song.Id = Song_Playlist.IdSong " +
                                            " GROUP BY IdPlaylist= '"+idPlaylist+"'");*/

        Cursor data = database.rawQuery("SELECT Song.Id,Song.Title,Song.ArtistName,Song.LinkData,Song.Size,Song.IdSong" +
                " FROM Song_Playlist,Song" +
                " WHERE Song.IdSong = Song_Playlist.IdSong " +
                " And IdPlaylist= '"+IdPlaylist+"'",null);
        //Cursor data = database.rawQuery("Select * from Song",null);
        songList.clear();
        while (data.moveToNext()){
            int id = data.getInt(0);
            String songTitle  = data.getString(1);
            String songArtist = data.getString(2);
            String songLink  = data.getString(3);
            String songSize = data.getString(4);
            long IdSong = data.getLong(5);
            songList.add(new Song(id, songTitle, songArtist, songLink, songSize,IdSong));
        }
        this.notifyDataSetChanged();
    }



}
