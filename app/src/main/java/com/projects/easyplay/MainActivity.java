package com.projects.easyplay;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] itemsAll;
    private ListView mSongsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSongsList=findViewById(R.id.songList);
        appStoragePermission();

    }

    public void appStoragePermission() {

        File file;
        File file2;
        File file3;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        displayAudio();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        checking();
    }

    private void checking()
    {
        File[] allFiles;
    }

    public ArrayList<File> readOnlyAudio(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] allFiles = file.listFiles();

        for (File individualFile : allFiles) {
            if (individualFile.isDirectory() && !individualFile.isHidden()) {
                arrayList.addAll(readOnlyAudio(individualFile));
            } else {
                if (individualFile.getName().endsWith(".mp3") || individualFile.getName().endsWith(".aac")) {
                    arrayList.add(individualFile);
                }
            }
        }

        return arrayList;
    }


    public void displayAudio()
    {
        final ArrayList<File> audioSongs=readOnlyAudio(Environment.getExternalStorageDirectory());

        itemsAll=new String[audioSongs.size()];

        for(int s=0;s<audioSongs.size();s++)
        {
            itemsAll[s]=audioSongs.get(s).getName();
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,itemsAll);
        mSongsList.setAdapter(arrayAdapter);

        mSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName=mSongsList.getItemAtPosition(i).toString();

                Intent intent=new Intent(MainActivity.this,SmartPlayerActivity.class);
                intent.putExtra("song",audioSongs);
                intent.putExtra("name",songName);
                intent.putExtra("position",i);
                startActivity(intent);

                //3.39
            }
        });

    }

}
