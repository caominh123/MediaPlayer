package com.lecaoliem.songlist.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lecaoliem.songlist.Adapter.SlidePageAdapter;
import com.lecaoliem.songlist.Fragment.NowPlayingPlaylistTab;
import com.lecaoliem.songlist.Fragment.ThumbnailTab;
import com.lecaoliem.songlist.Model.GlobalClass;
import com.lecaoliem.songlist.Model.Song;
import com.lecaoliem.songlist.R;
import com.lecaoliem.songlist.Service.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NowPlaying extends AppCompatActivity {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    AppCompatImageButton btnMode;
    AppCompatImageButton btnPrevious;
    AppCompatImageButton btnNext;
    AppCompatImageButton btnPlay;
    AppCompatImageButton btnVolume;
    SeekBar mSeekbarAudio;
    public MusicService musicService;
    TextView currentDuration;
    TextView maxDuration;
    boolean mUserIsSeeking = false;
    SeekBarAsyncTask seekBarAsyncTask;
    ThumbnailTab thumbnailTab;
    TextView txtTitle;
    TextView txtArtist;
    ImageButton btnBack;

    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);


        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        musicService = globalClass.getMusicService();

        initUI();
        initViewPager();
        initializeSeekBar();

        musicService.setPlaybackInfoListener(new PlaybackListener());
        btnVolume.setImageResource(R.drawable.ic_shuffle_24dp);//change
        // btnMode.setImageResource(R.drawable.ic_repeat_color);
        // Kiểm tra bài hát có thay đổi không
        if (musicService.isSongChanged ) {
            btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
            musicService.loadMedia();
        } else {
            txtTitle.setText(musicService.getSongCurrent().getmSongTitle());
            txtArtist.setText(musicService.getSongCurrent().getmArtistName());
            // Thiết lập max process seekBar và maxDuration
            musicService.initMaxDuration();
            // Đồng bộ currentDuration và SeekBar process

            if (musicService.isPlaying()) {
                // Chỉnh icon nút play
                btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                seekBarAsyncTask = new SeekBarAsyncTask(NowPlaying.this);
                seekBarAsyncTask.execute();
            } else {
                currentDuration.setText(dateFormat.format(musicService.getCurrentPosition()));
                mSeekbarAudio.setProgress(musicService.getCurrentPosition());
                btnPlay.setImageResource(R.drawable.ic_play_arrow_black_35dp);
            }

        }
    }
    private void initUI() {
        // Gắn view to button
        btnBack = findViewById(R.id.imageButtonBack);
        btnMode = findViewById(R.id.btnMode);
        btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);
        btnVolume = findViewById(R.id.btnShuffle);
        btnPrevious = findViewById(R.id.btnPrevious);
        mSeekbarAudio = findViewById(R.id.seekBar);
        currentDuration = findViewById(R.id.currentDuration);
        maxDuration = findViewById(R.id.maxDuration);
        pager = findViewById(R.id.viewPager);
        txtTitle = findViewById(R.id.textViewTitle);
        txtArtist = findViewById(R.id.textviewSongArtist);

        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMode.setImageResource(R.drawable.ic_repeat_black_24dp);
                btnVolume.setImageResource(R.drawable.ic_shuffle_color);
                musicService.shuffle();
            }
        });
        // Xử lý sự kiện onClick
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMode.setImageResource(R.drawable.ic_repeat_color);
                btnVolume.setImageResource(R.drawable.ic_shuffle_black_24dp);
                Toast.makeText(NowPlaying.this, "Repeat", Toast.LENGTH_SHORT).show();

            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NowPlaying.this, "Previous", Toast.LENGTH_SHORT).show();
                if (musicService != null){
                    btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    musicService.previous();
                    // Tắt đồng bộ
                    if(seekBarAsyncTask !=null && seekBarAsyncTask.getStatus() != AsyncTask.Status.PENDING){
                        seekBarAsyncTask.cancel(true);
                    }
                }

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicService != null) {
                    if (musicService.isPlaying()) {
                        Toast.makeText(NowPlaying.this, "Pause", Toast.LENGTH_SHORT).show();
                        btnPlay.setImageResource(R.drawable.ic_play_arrow_black_35dp);
                        musicService.pause();
                    } else {
                        Toast.makeText(NowPlaying.this, "Play", Toast.LENGTH_SHORT).show();
                        btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                        musicService.play();
                        seekBarAsyncTask = new SeekBarAsyncTask(NowPlaying.this);
                        seekBarAsyncTask.execute();

                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NowPlaying.this, "Next", Toast.LENGTH_SHORT).show();
                if (musicService != null) {
                    musicService.next();
                    btnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    // Tắt đồng bộ
                    if(seekBarAsyncTask !=null && seekBarAsyncTask.getStatus() != AsyncTask.Status.PENDING  ){
                        seekBarAsyncTask.cancel(true);
                    }
                }
            }
        });



    }
    private  void initViewPager(){
        // Xử lý pageView
        List<Fragment> list = new ArrayList<>();
        thumbnailTab = new ThumbnailTab();
        NowPlayingPlaylistTab nowPlayingPlaylistTab = new NowPlayingPlaylistTab();

        list.add(thumbnailTab);
        list.add(nowPlayingPlaylistTab);

        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        //stopService(playIntent);
        musicService = null;
        // musicService.setPlaybackInfoListener(null);
        if (seekBarAsyncTask != null && !seekBarAsyncTask.isCancelled()) {
            seekBarAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    private void initializeSeekBar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        musicService.seekTo(userSelectedPosition);
                    }
                });
    }


    public class PlaybackListener extends PlayBackInfoListener {

        @Override
        public void onPrepared() {
            seekBarAsyncTask = new SeekBarAsyncTask(NowPlaying.this);
            seekBarAsyncTask.execute();
        }

        @Override
        public void setDurationMax(int duration) {
            mSeekbarAudio.setMax(duration);
            maxDuration.setText(dateFormat.format(duration));
            Log.e("Now Playing", String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                mSeekbarAudio.setProgress(position);
                currentDuration.setText(dateFormat.format(position));
                Log.e("Now Playing", String.format("setPlaybackPosition: setProgress(%d)", position));
            }
        }

        @Override
        public void setInforSong(Song song, int duration) {
            mSeekbarAudio.setMax(duration);
            maxDuration.setText(dateFormat.format(duration));
            Log.e("Now Playing", String.format("setPlaybackDuration: setMax(%d)", duration));
            txtTitle.setText(song.getmSongTitle());
            txtArtist.setText(song.getmArtistName());
        }

    }

    public class SeekBarAsyncTask extends AsyncTask<Void, Integer, Void> {

        Activity contextParent;

        public SeekBarAsyncTask(Activity contextParent) {
            this.contextParent = contextParent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("AsyncTask", "Start AsyncTask");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (musicService.isPlaying()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("AsyncTask", "Error");
                }

                publishProgress(musicService.getCurrentPosition());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // TextView currentDuration = (TextView)contextParent.findViewById(R.id.currentDuration);
            int position = values[0];
            currentDuration.setText(dateFormat.format(position));
            mSeekbarAudio.setProgress(position);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("AsyncTask", "Finished");
        }
    }









    /*
    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(NowPlaying.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }*/
}
