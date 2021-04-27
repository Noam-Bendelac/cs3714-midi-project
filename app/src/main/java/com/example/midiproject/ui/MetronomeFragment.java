package com.example.midiproject.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.midiproject.R;

public class MetronomeFragment extends Fragment implements View.OnClickListener {
    private Button play;
    private EditText bpm;
    int delay = 0;

    private static MyAsyncTask myAsyncTask;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_metronome, container, false);

        play = root.findViewById(R.id.playButton);
        play.setOnClickListener(this);
        bpm = root.findViewById(R.id.editBpm);

        return root;
    }

    private void stopMetronome() {
        delay = 0;
        myAsyncTask.cancel(true);
    }

    private void playMetronome(int bpm) {
        if (bpm != 0) {
            delay = (60000 / bpm);
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute(delay);
        }
    }

    public String changeText(){
        if (play.getText().equals("Play")) {
            play.setText("Pause");
        }
        else {
            play.setText("Play");
        }
        return (String) play.getText();
    }

    public void onDestroy() {
        super.onDestroy();
        if (myAsyncTask != null) {
            myAsyncTask.cancel(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (changeText().equals("Pause")){
            playMetronome(Integer.parseInt(String.valueOf(bpm.getText())));
        }
        else {
            stopMetronome();
        }
    }

    private static class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {


        @Override
        protected Void doInBackground(Integer... params) {
            while(true){
                try{
                    //checking if the asynctask has been cancelled, end loop if so
                    if(isCancelled()) break;

                    Thread.sleep(params[0]);

                    // TODO make the music player play the metronome sounds

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}