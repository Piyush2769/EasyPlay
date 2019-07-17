package com.projects.easyplay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class SmartPlayerActivity extends AppCompatActivity {

    private RelativeLayout parentRelative;
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private String keeper="";

    private ImageView imageView;
    private ImageView previousBtn,playBtn,nextBtn;
    private TextView songNametxt;
    private RelativeLayout lowerRelativeLayout;
    private Button voiceEnabled;
    private String mode="ON";

    private MediaPlayer myMediaPlayer;
    private int position;
    private ArrayList<File> mySongs;
    private String mSongName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkPermission();

        playBtn=findViewById(R.id.playPauseBtn);
        previousBtn=findViewById(R.id.previousBtn);
        nextBtn=findViewById(R.id.nextBtn);
        voiceEnabled=findViewById(R.id.voice_enabled);
        lowerRelativeLayout=findViewById(R.id.lower);
        imageView=findViewById(R.id.logo);
        songNametxt=findViewById(R.id.songName);



        parentRelative=findViewById(R.id.parentRelative);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(SmartPlayerActivity.this);
        speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        validatingAndStartPlaying();
        imageView.setBackgroundResource(R.drawable.logo);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matchesFound=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matchesFound!=null)
                {
                    keeper=matchesFound.get(0);

                    if(keeper.equals("pause the song"))
                    {
                        pausePlaySong();
                        Toast.makeText(SmartPlayerActivity.this, "Command="+keeper, Toast.LENGTH_SHORT).show();
                    }

                    else if(keeper.equals("play the song"))
                    {
                        pausePlaySong();
                        Toast.makeText(SmartPlayerActivity.this, "Command="+keeper, Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        parentRelative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechIntent);
                        keeper="";
                        break;

                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }

                return false;
            }
        });

        voiceEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("ON"))
                {
                    mode="OFF";
                    voiceEnabled.setText("Voice Enabled Mode- OFF");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mode="ON";
                    voiceEnabled.setText("Voice Enabled Mode- ON");
                    lowerRelativeLayout.setVisibility(View.GONE);
                }

            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausePlaySong();
            }
        });



    }


    private void validatingAndStartPlaying()
    {
        if(myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("song");
        mSongName=mySongs.get(position).getName();

        String songName=intent.getStringExtra("name");

        songNametxt.setText(songName);
        songNametxt.setSelected(true);

        position=bundle.getInt("position",0);
        Uri uri=Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(SmartPlayerActivity.this,uri);
        myMediaPlayer.start();

    }


    private void checkPermission()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(SmartPlayerActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED))
            {
                Intent i=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                startActivity(i);
                finish();
            }
        }
    }


    private void pausePlaySong()
    {
        imageView.setBackgroundResource(R.drawable.four);

        if(myMediaPlayer.isPlaying())
        {
            playBtn.setBackgroundResource(R.drawable.play);
            myMediaPlayer.pause();
        }
        else
        {
            playBtn.setBackgroundResource(R.drawable.pause);
            myMediaPlayer.start();

            imageView.setBackgroundResource(R.drawable.five);
        }
    }











}
