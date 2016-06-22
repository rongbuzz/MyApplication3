package com.example.user.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
        TextView tv;
        int position;
        ArrayList<File> songlist;
        SeekBar sb;
        Button pre;
        Button back;
        Button playpause;
        Button front;
        Button next;
        Thread updateSeekbar;
    static MediaPlayer mp ;
        Uri u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        tv = (TextView) findViewById(R.id.textView);
        pre = (Button) findViewById(R.id.pre);
        back = (Button) findViewById(R.id.back);
        playpause = (Button) findViewById(R.id.playpause);
        front = (Button) findViewById(R.id.front);
        next = (Button) findViewById(R.id.next) ;

        //setOnClick
        pre.setOnClickListener(this);
        back.setOnClickListener(this);
        playpause.setOnClickListener(this);
        front.setOnClickListener(this);
        next.setOnClickListener(this);

        //seekbar
        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition= 0;

                while(currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition =mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        // checking is player running
        if(mp!= null){
            mp.stop();
            mp.start();
        }

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        songlist = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);




        //player is here
        u = Uri.parse(songlist.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();

        sb.setMax(mp.getDuration());


        //start seekbar thread
        updateSeekbar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

         //set name
        tv.setText(songlist.get(position).getName());


      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.playpause :
                if(mp.isPlaying()){
                    playpause.setText(">");
                    mp.pause();
                }else{
                    mp.start();
                    playpause.setText("||");
                }
                break;
            case R.id.front:
                mp.seekTo(mp.getCurrentPosition()+ 5000);
                break;
            case R.id.back:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.pre:
                mp.stop();
                mp.release();
                position = (position-1 <0)? songlist.size()-1:position-1;
                u = Uri.parse(songlist.get(position).toString());
                tv.setText(songlist.get(position).getName());
                mp = MediaPlayer.create(getApplicationContext(),u);
                sb.setMax(mp.getDuration());
                mp.start();
                break;
            case R.id.next:
                mp.stop();
                mp.release();
                position = (position+1)%songlist.size();
                u = Uri.parse(songlist.get(position).toString());
                tv.setText(songlist.get(position).getName());
                mp = MediaPlayer.create(getApplicationContext(),u);
                sb.setMax(mp.getDuration());
                mp.start();
                break;


        }


    }
}
