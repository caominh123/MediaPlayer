package com.lecaoliem.songlist.Adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Model.Playlist;
import com.lecaoliem.songlist.R;

import java.util.List;

public class PlaylistAdapters extends BaseAdapter  {
    private SQLiteDatabase database;
    private Context context;
    private int layout;
    private List<Playlist> playlists;
    //private Database database;

    public PlaylistAdapters(Context context, int layout, List<Playlist> playlists, SQLiteDatabase database) {
        this.context = context;
        this.layout = layout;
        this.playlists = playlists;
        this.database = database;

    }
    @Override
    public int getCount() {
        return playlists.size();
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
        TextView txtPlaylistName, txtSongNumber;
        ImageView imgHinhPlaylist;
        ImageView imgMenuPlaylist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            // anh xa
            holder.txtPlaylistName = (TextView) convertView.findViewById(R.id.textviewTenPlaylist);
            holder.txtSongNumber = (TextView) convertView.findViewById(R.id.textviewNumberSong);
            holder.imgHinhPlaylist = (ImageView) convertView.findViewById(R.id.imageviewHinhPlaylist);
            holder.imgMenuPlaylist = (ImageView)convertView.findViewById(R.id.imageviewMenuPlaylist) ;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //gan gia tri
        final Playlist playlist = playlists.get(position);
        holder.txtPlaylistName.setText(playlist.getmPlaylistName());
        holder.txtSongNumber.setText(getSongNumber(playlist.getMidPlayList())+" songs");
        holder.imgHinhPlaylist.setImageResource(R.drawable.playlist);

        holder.imgMenuPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, holder.txtSongTitle.getText(), Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(context,holder.imgMenuPlaylist);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_playlist,popupMenu.getMenu());
                //event item menu click
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuEditNamePlaylist:
                                DialogEditNamePlayList(playlist.getMidPlayList(),playlist.getmPlaylistName());
                                break;
                            case R.id.menuDeleteItemPlaylist:
                                DialogDeletePlaylistItem(playlist.getMidPlayList(),playlist.getmPlaylistName());
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
    public int getSongNumber(int idPlaylist){
        //Cập nhật số lượng
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM Song_Playlist WHERE IdPlaylist ='"+idPlaylist+"'",
                null);
        cursor.moveToNext();
        int songNumber = cursor.getInt(0);
        /*
        ContentValues value = new ContentValues();
        value.put("SongNumber",songNumber);
        database.update("Playlist",value,"Id = ?", new String[]{idPlaylist + ""});*/
        return songNumber;
    }

    public void DialogDeletePlaylistItem(final int id, String name){
        AlertDialog.Builder dialogInfo = new AlertDialog.Builder(context);
        dialogInfo.setMessage("Do you want to delete "+ name +"?");
        dialogInfo.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.delete("Playlist", "Id = ? ", new String[] {id + ""} );
                //database.QueryData("DELETE FROM Playlist WHERE Id = '"+ id +"'");
                GetDataPlaylist();

                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        });
        dialogInfo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogInfo.show();
    }
    public void DialogEditNamePlayList(final int id, final String name){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_name_playlist);
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtNameEditPlaylist = (EditText) dialog.findViewById(R.id.edittextEditPlaylistName);
        Button btnCancle = (Button) dialog.findViewById(R.id.buttonCancleEdit);
        Button btnOk = (Button) dialog.findViewById(R.id.buttonOkEdit);
        edtNameEditPlaylist.setText(name);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlistName = edtNameEditPlaylist.getText().toString();
                if(playlistName.equals("")){
                    Toast.makeText(context, "Please enter the playlist name!", Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("PlaylistName",playlistName);

                    database.update("Playlist",contentValues,"Id = ?", new String[]{id + ""});

                    //database.QueryData("UPDATE Playlist SET PlaylistName = '"+ playlistName +"' WHERE Id = '"+ id +"'");
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataPlaylist();
                }
            }
        });
        dialog.show();
    }

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
        this.notifyDataSetChanged();
    }
}
