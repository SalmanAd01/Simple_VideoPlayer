package com.example.video_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Video extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play,pause,next,previous;
    SeekBar seekBar;
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;
    ArrayList <File> videos;
    Thread updateSeek;
    String textcontent;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        textView = findViewById(R.id.textView);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        seekBar = findViewById(R.id.seekBar);
        surfaceView = findViewById(R.id.surfaceView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        videos = (ArrayList)bundle.getParcelableArrayList("videolist");
        textcontent = intent.getStringExtra("currentvideo");
        textView.setText(textcontent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(videos.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        surfaceView.setKeepScreenOn(true);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentposition = 0;
                try{
                    while(currentposition < mediaPlayer.getDuration()){
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    pause.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                surfaceView.setVisibility(View.INVISIBLE);
                if(position != 0){
                    position = position -1;
                }
                else{
                    position = videos.size() - 1;
                }
                Uri uri = Uri.parse(videos.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                textcontent = videos.get(position).getName().toString();
                textView.setText(textcontent);
                surfaceView.setKeepScreenOn(true);
                surfaceView.setVisibility(View.VISIBLE);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                        mediaPlayer.setDisplay(surfaceHolder);
                    }
                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }
                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    }
                });
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                surfaceView.setVisibility(View.INVISIBLE);
                if(position != videos.size() - 1){
                    position = position +1;
                }
                else{
                    position = 0;
                }
                Uri uri = Uri.parse(videos.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                textcontent = videos.get(position).getName().toString();
                textView.setText(textcontent);
                surfaceView.setKeepScreenOn(true);
                surfaceView.setVisibility(View.VISIBLE);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                        mediaPlayer.setDisplay(surfaceHolder);
                    }
                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }
                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    }
                });
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });

    }
}