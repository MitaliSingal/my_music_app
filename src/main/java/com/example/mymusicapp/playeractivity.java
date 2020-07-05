package com.example.mymusicapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playeractivity extends AppCompatActivity {
    Button pause,next,previous;
    TextView songname;
    SeekBar seekbar;
    static MediaPlayer mymediaplayer;
    int position;
    ArrayList<File> mysongs;
    String s;
    Thread updateseekbar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);
        pause=(Button)findViewById(R.id.pause);
        previous=(Button)findViewById(R.id.previous);
        next=(Button)findViewById(R.id.next);
        songname =(TextView)findViewById(R.id.songname);
        seekbar=(SeekBar)findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        updateseekbar = new Thread(){
            @Override
            public void run() {
                int totalduration = mymediaplayer.getDuration();
                int currentposition = 0;
                while(currentposition<totalduration){
                    try {
                        sleep(500);
                        currentposition = mymediaplayer.getCurrentPosition();
                        seekbar.setProgress(currentposition);

                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }


            }
        };
        if(mymediaplayer!=null){
            mymediaplayer.stop();
            mymediaplayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();


        mysongs= (ArrayList) bundle.getParcelableArrayList("songs");

        s= mysongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");
        songname.setText(songName);
        songname.setSelected(true);

        position = bundle.getInt("pos",0);
        Uri u = Uri.parse(mysongs.get(position).toString());


        mymediaplayer = MediaPlayer.create(getApplicationContext(),u);
        mymediaplayer.start();
        seekbar.setMax(mymediaplayer.getDuration());

        updateseekbar.start();

        seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        seekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mymediaplayer.seekTo(seekBar.getProgress());
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbar.setMax(mymediaplayer.getDuration());
                if(mymediaplayer.isPlaying()){
                    pause.setBackgroundResource(R.drawable.playicon);
                    mymediaplayer.pause();
                }
                else {
                    pause.setBackgroundResource(R.drawable.pauseicon);
                    mymediaplayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.stop();
                mymediaplayer.release();
                position = ((position+1)%mysongs.size());

                Uri u = Uri.parse(mysongs.get(position).toString());

                mymediaplayer = MediaPlayer.create(getApplicationContext(),u);

                s = mysongs.get(position).getName().toString();
                songname.setText(s);

                mymediaplayer.start();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.stop();
                mymediaplayer.release();
                position=((position-1)<0)?(mysongs.size()-1):(position-1);


                Uri u = Uri.parse(mysongs.get(position).toString());
                mymediaplayer =MediaPlayer.create(getApplicationContext(),u);
                s = mysongs.get(position).getName().toString();
                songname.setText(s);

                mymediaplayer.start();
            }



        });





    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }
}
