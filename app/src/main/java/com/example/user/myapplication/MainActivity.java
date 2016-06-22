package com.example.user.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView musicLV;
    String[] item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicLV = (ListView) findViewById(R.id.musicListView);

        final ArrayList<File> songlist = findSongs(Environment.getExternalStorageDirectory());

        item = new String[songlist.size()];

        for (int i = 0;i<songlist.size(); i++){
           //toast(songlist.get(i).getName().toString());
            item[i] = songlist.get(i).getName().toString().replace(".mp3"," ");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item_custom,R.id.textView,item);
        musicLV.setAdapter(adp);
        musicLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent( getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",songlist));
            }
        });
    }

    public ArrayList<File> findSongs(File root){
       ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if (singleFile.isDirectory()&& !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }else {
                if(singleFile.getName().endsWith(".mp3")|| singleFile.getName().endsWith(".wav")){
                al.add(singleFile);
                }
            }
        }
        return al;
    }
    public  void toast (String string){
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
}
