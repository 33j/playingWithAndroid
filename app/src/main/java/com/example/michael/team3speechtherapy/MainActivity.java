package com.example.michael.team3speechtherapy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaActionSound;
import android.media.MediaRecorder;
import android.os.Vibrator;
import android.service.vr.VrListenerService;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;


import java.io.BufferedWriter;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private Button mRecord;
    private Button mStop;
    private ListView list;
    //private Button hist;
    private Vibrator myVib;
    private MediaActionSound mySound;

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final String FILE_PATH = "/sdcard/Test.pcm";
    private Boolean isRecording = false;
    private String currWord = null;
    private Thread recordingThread = null;
    private AudioRecord record = null;
    private static final String COMMA_DELIMITER = ",";
    int BufferElements2Rec = 1024;
    int BytesPerElement = 2;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        MediaActionSound mySound = new MediaActionSound();

        mySound.play(MediaActionSound.SHUTTER_CLICK);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);
        //initializeButtons();
        list = findViewById(R.id.words);
        setListeners();
        createFile();
    }

    public void initializeButtons() {
       // mRecord = (Button) findViewById(R.id.playButton);
        //mStop = (Button) findViewById(R.id.stopButton);
        //hist = (Button) this.findViewById(R.id.btn1);
    }

    private void vibr() {
        myVib.vibrate(150);
    }
    public void setListeners() {
        final Context context = this;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isRecording) return;
                MediaActionSound mySound = new MediaActionSound();
                mySound.play(MediaActionSound.START_VIDEO_RECORDING);
                isRecording = true;
                currWord = (String)parent.getAdapter().getItem(position);


                try
                {
                    Thread.sleep(3000);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }

                double[] formants = recorderRaw();
                double score = Feedback.score(formants[0],formants[1],"M",getResources().getStringArray(R.array.vowels)[position]);
                try {
                    //Jamie's Alg
                    String visual = Feedback.simluationFeedback(formants[0],formants[1],"M",getResources().getStringArray(R.array.vowels)[position]);

                    String F1=String.valueOf((int)formants[0]);
                    String F2=String.valueOf((int)formants[1]);
                    String Score= String.valueOf((int)Math.floor(score));
                    launchInstantFeedbackActivity(currWord,getResources().getStringArray(R.array.vowels)[position],F1,F2,Score, visual );
                    //updates user stats
                    changeUserFile(formants[0],formants[1],"Good",getResources().getStringArray(R.array.vowels)[position]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isRecording = false;
            }
        });
    }

    public void launchInstantFeedbackActivity(String word, String Vowel, String F1, String F2, String rating, String recommend){
        vibr();
        Intent i = new Intent(this, InstantFeedbackActivity.class);
        i.putExtra("word", word);
        i.putExtra("vowel", Vowel);
        i.putExtra("F1", F1);
        i.putExtra("F2",F2);
        i.putExtra("rating",rating);
        i.putExtra("recommend",recommend);

        startActivity(i);
    }

    public void launchHistoryActivity(View view) {
        vibr();
        Intent intent = new Intent(this, HistoryActivity.class);
        MediaActionSound mySound = new MediaActionSound();
        mySound.play(MediaActionSound.SHUTTER_CLICK);
        startActivity(intent);
    }

    public void launchAboutActivity(View view)
    {
        vibr();
        Intent intent = new Intent(this, AboutActivity.class);
        MediaActionSound mySound = new MediaActionSound();
        mySound.play(MediaActionSound.SHUTTER_CLICK);
        startActivity(intent);

    }

    double[] recorderRaw() {

        int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {

                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                             record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);



                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        //Requesting record permisions
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
            }
        }


        record.startRecording();
        Toast toast = Toast.makeText(getApplicationContext(), "Started Recording", Toast.LENGTH_SHORT);
        toast.show();

        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
            try {
                Thread.sleep(1000);
                return stopRecording();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        double[] stopRecording() throws IOException {
            if (null != record) {
                isRecording = false;
                record.stop();
                record.release();
                record = null;
                recordingThread = null;
                Toast toast = Toast.makeText(getApplicationContext(), "Stopped Recording", Toast.LENGTH_SHORT);
                toast.show();
            }
            //try {
             //   return getFormants();
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
            double F1 = Math.random() * 1000;
            double F2 = Math.random() * 2500;

            return new double[]{F1,F2};
        }

        private void getFormants() throws IOException {
            byte[] bData;
            short[] sData;
            double[] dData;
            bData = readAudioDatafromFile();
            sData = byte2short(bData);
            dData = short2double(sData);
            double[][] formants;
            double[] tmp;
           // HammingWindow hamm = new HammingWindow(dData.length); // FIX PARAMETERS
           // tmp = hamm.applyFunction(dData);
            //LinearPredictiveCoding lpc = new LinearPredictiveCoding(dData.length, 2); // FIX PARAMETERS
            //formants = lpc.applyLinearPredictiveCoding(tmp);

            //tmpwrite(formants);
        }

        private void tmpwrite(double[][] d) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/sdcard/hi.txt"));
            for (int i = 0; i < d.length; i++) {
                for (int j = 0; j < d.length; j++)
                    writer.write(Double.toString(d[i][j])+"\t");
                writer.write("\n");
            }
            writer.close();
        }

        private byte[] readAudioDatafromFile() throws IOException {
            File f = new File(FILE_PATH);
            byte byteData[] = new byte[(int)f.length() / Byte.SIZE];
            FileInputStream is = null;
            try {
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            is.read(byteData);
            is.close();
            return byteData;
        }

        private void createFile(){
            File internalStorageDir = getFilesDir();
            try {
                FileOutputStream file = openFileOutput("alice.csv", MODE_APPEND);
                OutputStreamWriter osw = new OutputStreamWriter(file);
                osw.flush();
                osw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void changeUserFile(double f1,double f2, String Score, String vowel) throws IOException {
            String COMMA_DELIMITER = ",";
            String NEW_LINE_SEPARATOR = "\n";
            FileOutputStream fos = openFileOutput("alice.csv",MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy ");
            osw.append(dateFormat.format(currentTime));
            //osw.append(dateFormat.format(currentTime.toString()));
            osw.append(COMMA_DELIMITER);
            osw.append(String.valueOf((int)f1));
            osw.append(COMMA_DELIMITER);
            osw.append(String.valueOf((int)f2));
            osw.append(COMMA_DELIMITER);
            osw.append(Score);
            osw.append(COMMA_DELIMITER);
            osw.append(vowel);
            osw.append(NEW_LINE_SEPARATOR);
            osw.flush();
            osw.close();
        }

        private void writeAudioDataToFile() {
            // Write the output audio in byte
            short sData[] = new short[BufferElements2Rec];

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(FILE_PATH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (isRecording) {
                // gets the voice output from microphone to byte format

                record.read(sData, 0, BufferElements2Rec);
                System.out.println("Short writing to file" + sData.toString());
                try {
                    // // writes the data to file from buffer
                    // // stores the voice buffer
                    byte bData[] = short2byte(sData);
                    os.write(bData, 0, BufferElements2Rec * BytesPerElement);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void verifyStoragePermissions(Activity activity) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }

        private byte[] short2byte(short[] sData) {
            int shortArrsize = sData.length;
            byte[] bytes = new byte[shortArrsize * 2];
            for (int i = 0; i < shortArrsize; i++) {
                bytes[i * 2] = (byte) (sData[i] & 0x00FF);
                bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
                sData[i] = 0;
            }
            return bytes;
        }

        private short[] byte2short(byte[] byteData) {
            int byteArrsize = byteData.length;
            short tmp;
            short[] shortData = new short[byteArrsize / 2];
            for (int i = 0; i < byteArrsize / 2; i++) {
                shortData[i] = (short) byteData[i*2];
                tmp = (short) (byteData[(i*2) + 1] >> 8);
                shortData[i] += tmp;
            }
            return shortData;
        }

        private double[] short2double (short[] shortData) {
            double[] doubleData = new double[shortData.length];
                    for (int i = 0; i < shortData.length; i++)
                        doubleData[i] = (double) shortData[i];
            return doubleData;
        }
    }
