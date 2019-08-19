package com.example.bluefield;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class Explaination extends AppCompatActivity {

    VideoView videoView;
    int startPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explanation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        videoView=findViewById(R.id.videoView);

        String videoPath = "android.resource://com.example.bluefield/"+R.raw.bluefield;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        startPosition=videoView.getCurrentPosition();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void Video(View v) {
        if (videoView.isPlaying()) videoView.pause();
        else videoView.start();
    }

    public void Replay(View v){
        videoView.seekTo(startPosition);
        videoView.start();
    }

    public void Accueil(View v){
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }

    public void Ted(View v){
        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://ed.ted.com/"));
        startActivity(intent);
    }

}
