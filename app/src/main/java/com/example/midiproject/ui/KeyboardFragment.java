package com.example.midiproject.ui;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.midiproject.R;
import com.example.midiproject.model.Instrument;
import com.example.midiproject.model.MidiConnection;

public class KeyboardFragment extends Fragment {

    private MidiConnection mMidiConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMidiConnection = MidiConnection.getInstance(getContext());
        mMidiConnection.setInstrument(Instrument.PIANO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);


        setupUI(root);

        return root;
    }

    private void setupUI(View root) {
        LinearLayout layout = (LinearLayout) root.findViewById(R.id.linearLayout);
        int[] pitches = Instrument.PIANO.getValidPitches();
        layout.setOrientation(LinearLayout.HORIZONTAL);


        for(int j = 0; j < pitches.length; j++){
            Button btnTag = new Button(requireContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            btnTag.setLayoutParams(p);
            switch(j % 7){
                case 0:
                    btnTag.setText("C");
                    break;
                case 1:
                    btnTag.setText("D");
                    break;
                case 2:
                    btnTag.setText("E");
                    break;
                case 3:
                    btnTag.setText("F");
                    break;
                case 4:
                    btnTag.setText("G");
                    break;
                case 5:
                    btnTag.setText("A");
                    break;
                case 6:
                    btnTag.setText("B");
                    break;
            }
            int pitch = pitches[j];
            btnTag.setOnTouchListener((v, event) -> {
                switch(event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mMidiConnection.sendNoteOn(pitch, 127);
                        btnTag.setPressed(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mMidiConnection.sendNoteOff(pitch);
                        btnTag.setPressed(true);
                        break;
                }
                return true;
            });

            layout.addView(btnTag);
        }


    }
}
