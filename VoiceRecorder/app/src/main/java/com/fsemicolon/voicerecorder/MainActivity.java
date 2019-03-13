package com.fsemicolon.voicerecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //TODO 1:Set output File String
    //TODO 2:Checkout Activity Compat
    //TODO 3:Create onClickListener for all the buttons

    private Button recordButton, playButton, stopButton;

    private String outputFileName = null;
    private MediaRecorder mediaRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = findViewById(R.id.record_button);
        playButton = findViewById(R.id.play_button);
        stopButton = findViewById(R.id.stop_button);

        playButton.setEnabled(false);
        stopButton.setEnabled(false);

        mediaRecorder = new MediaRecorder();

        outputFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFileName);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean audioPermission = false;
                boolean storagePermission = false;

                if (checkAudioPermission()) {
                    audioPermission = true;
                }

                if (checkExternalStoragePermission()) {
                    storagePermission = true;
                }


                if (audioPermission && storagePermission) {


                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException ise) {
                        Log.d("exception", "" + ise);
                    } catch (IOException ioe) {
                        Log.d("exception", "" + ioe);
                    }
                    recordButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                recordButton.setEnabled(true);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio Recording Stopped", Toast.LENGTH_SHORT).show();

            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(outputFileName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("exception", "" + e);
                }
            }
        });

    }

    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
        return true;
    }

    private boolean checkExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);

        }
        return true;
    }
}
