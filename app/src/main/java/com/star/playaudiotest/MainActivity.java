package com.star.playaudiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private Button mPlay;
    private Button mPause;
    private Button mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlay = findViewById(R.id.play);
        mPause = findViewById(R.id.pause);
        mStop = findViewById(R.id.stop);

        mPlay.setOnClickListener(v -> {

            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        });

        mPause.setOnClickListener(v -> {

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        });

        mStop.setOnClickListener(v -> {

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                initMediaPlayer();
            }
        });

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            initMediaPlayer();
        }
    }

    private void initMediaPlayer() {

        try {
            File file = new File(Environment.getExternalStorageDirectory(), "童话镇.mp3");

            mMediaPlayer.setDataSource(file.getPath());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE:
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {

            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
